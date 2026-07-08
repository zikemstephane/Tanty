import os
import re
from pathlib import Path
from collections import defaultdict

# Paths
BACKEND_ROOT = Path("C:/Users/User/Downloads/Api_tanty/src/main/java/com/NTFOODS/Api_tanty")
FRONTEND_ROOT = Path("C:/Users/User/Downloads/Api_tanty/frontend_erp_tanty_architecture/frontend_erp_tanty_architecture/frontend_erp_tanty/src")
OUTPUT = Path("C:/Users/User/Downloads/Api_tanty/architecture_du_projet.md")


def read_text(path):
    try:
        with open(path, "r", encoding="utf-8") as f:
            return f.read()
    except Exception as e:
        return f""  # skip unreadable


def extract_package(source):
    m = re.search(r'package\s+([a-zA-Z_][a-zA-Z0-9_.]*)\s*;', source)
    return m.group(1) if m else None


def remove_comments_and_strings(source):
    # Remove line comments
    s = re.sub(r'//.*?\n', '\n', source)
    # Remove block comments (non-greedy)
    s = re.sub(r'/\*.*?\*/', '', s, flags=re.DOTALL)
    # Replace string literals with placeholder to avoid parsing issues
    s = re.sub(r'"(?:\\.|[^"\\])*"', '""', s)
    s = re.sub(r"'(?:\\.|[^'\\])*'", "''", s)
    return s


def extract_top_level_declarations(source):
    """Extract class, interface, enum, @interface, record declarations with their type and name."""
    cleaned = remove_comments_and_strings(source)
    # Pattern to match public/protected/private/package-private class/interface/enum/annotation/record
    pattern = re.compile(
        r'(?P<mods>(?:\s*(?:public|protected|private|static|abstract|final|@\w+(?:\([^)]*\))?))*?)\s*'
        r'(?P<kind>class|interface|enum|@interface|record)\s+'
        r'(?P<name>[A-Z][a-zA-Z0-9_]*)'
        r'(?:\s*<[^>]*>)?'  # generics
        r'(?:\s*\((?P<recordParams>[^)]*)\))?'  # record components
        r'(?:\s+extends\s+(?P<extends>[^{]+?))?'
        r'(?:\s+implements\s+(?P<implements>[^{]+?))?'
        r'\s*\{',
        re.MULTILINE
    )
    decls = []
    for m in pattern.finditer(cleaned):
        kind = m.group('kind')
        name = m.group('name')
        mods = m.group('mods').strip()
        extends = m.group('extends')
        implements = m.group('implements')
        recordParams = m.group('recordParams')
        decls.append({
            'kind': kind,
            'name': name,
            'modifiers': mods,
            'extends': extends.strip() if extends else None,
            'implements': implements.strip() if implements else None,
            'recordParams': recordParams.strip() if recordParams else None,
            'start': m.start()
        })
    return decls


def find_matching_brace(source, start_idx):
    """Find index of matching } assuming start_idx is at {."""
    depth = 0
    for i in range(start_idx, len(source)):
        if source[i] == '{':
            depth += 1
        elif source[i] == '}':
            depth -= 1
            if depth == 0:
                return i
    return len(source)


def extract_fields(body):
    """Extract field declarations from a class body."""
    fields = []
    # Split by semicolons but avoid inside braces of methods
    # We need to capture field declarations which end with ; and are not in method bodies
    # A simpler approach: match lines/segments ending with ; that are not method calls/declarations
    # We'll use a regex that looks for modifiers + type + names + ;
    pattern = re.compile(
        r'(?P<annotations>(?:\s*@\w+(?:\([^)]*\))?\s*)*)'
        r'(?P<mods>(?:\s*(?:public|protected|private|static|final|volatile|transient))*\s*)'
        r'(?P<type>[A-Z][a-zA-Z0-9_<>,?\[\]\s]*|[a-z][a-zA-Z0-9_<>,?\[\]\s]*?)\s+'
        r'(?P<names>[a-zA-Z_][a-zA-Z0-9_]*(?:\s*=\s*[^;]+)?(?:\s*,\s*[a-zA-Z_][a-zA-Z0-9_]*(?:\s*=\s*[^;]+)?)*)\s*;',
        re.MULTILINE
    )
    for m in pattern.finditer(body):
        names = m.group('names')
        # Skip if it looks like a method declaration (has parentheses after name)
        if re.search(r'\b[a-zA-Z_][a-zA-Z0-9_]*\s*\(', names):
            continue
        # Skip obvious non-field types (keywords)
        if m.group('type').strip() in ('return', 'if', 'while', 'for', 'switch', 'catch', 'throw', 'new'):
            continue
        fields.append({
            'annotations': m.group('annotations').strip(),
            'modifiers': m.group('mods').strip(),
            'type': m.group('type').strip(),
            'names': names.strip()
        })
    return fields


def extract_methods(body):
    """Extract method declarations from a class body."""
    methods = []
    # Match method signatures: modifiers + return type + name(params) throws? {
    pattern = re.compile(
        r'(?P<annotations>(?:\s*@\w+(?:\([^)]*\))?\s*)*)'
        r'(?P<mods>(?:\s*(?:public|protected|private|static|abstract|final|synchronized|native))*\s*)'
        r'(?P<return>[A-Z][a-zA-Z0-9_<>,?\[\]\s]*|[a-z][a-zA-Z0-9_<>,?\[\]\s]*?)\s+'
        r'(?P<name>[a-zA-Z_][a-zA-Z0-9_]*)\s*'
        r'\((?P<params>[^)]*)\)'
        r'(?:\s+throws\s+(?P<throws>[^{]+?))?\s*\{',
        re.MULTILINE
    )
    statement_keywords = {'return', 'if', 'while', 'for', 'switch', 'catch', 'throw', 'new', 'else', 'try'}
    for m in pattern.finditer(body):
        ret = m.group('return').strip()
        name = m.group('name')
        if ret in statement_keywords or name in statement_keywords:
            continue
        methods.append({
            'annotations': m.group('annotations').strip(),
            'modifiers': m.group('mods').strip(),
            'return': ret,
            'name': name,
            'params': m.group('params').strip(),
            'throws': m.group('throws').strip() if m.group('throws') else None
        })
    return methods


def extract_enum_constants(body):
    """Extract enum constants from enum body."""
    # Find first ; and get constants before it
    first_semi = body.find(';')
    if first_semi == -1:
        return []
    consts_part = body[:first_semi]
    # Remove comments
    consts_part = re.sub(r'//.*?\n', '\n', consts_part)
    consts_part = re.sub(r'/\*.*?\*/', '', consts_part, flags=re.DOTALL)
    # Split by comma, but be careful with parentheses
    constants = []
    current = ""
    depth = 0
    for ch in consts_part:
        if ch == '(':
            depth += 1
        elif ch == ')':
            depth -= 1
        elif ch == ',' and depth == 0:
            constants.append(current.strip())
            current = ""
            continue
        current += ch
    if current.strip():
        constants.append(current.strip())
    return [c for c in constants if c]


def parse_java_file(path):
    source = read_text(path)
    if not source:
        return None
    package = extract_package(source)
    decls = extract_top_level_declarations(source)
    result = []
    cleaned = remove_comments_and_strings(source)
    for decl in decls:
        # Find body of this declaration
        start_idx = cleaned.find('{', decl['start'])
        if start_idx == -1:
            continue
        end_idx = find_matching_brace(cleaned, start_idx)
        body = cleaned[start_idx:end_idx+1]
        if decl['kind'] == 'enum':
            constants = extract_enum_constants(body)
        else:
            constants = []
        fields = extract_fields(body)
        methods = extract_methods(body)
        result.append({
            'package': package,
            'kind': decl['kind'],
            'name': decl['name'],
            'modifiers': decl['modifiers'],
            'extends': decl['extends'],
            'implements': decl['implements'],
            'recordParams': decl.get('recordParams'),
            'constants': constants,
            'fields': fields,
            'methods': methods
        })
    return result


def parse_ts_interfaces_and_classes(path):
    source = read_text(path)
    if not source:
        return []
    items = []
    # Extract interfaces
    interface_pattern = re.compile(
        r'export\s+interface\s+(?P<name>[A-Za-z_][A-Za-z0-9_]*)\s*(?:extends\s+(?P<extends>[^{]+))?\s*\{',
        re.MULTILINE
    )
    for m in interface_pattern.finditer(source):
        start = m.end() - 1
        end = find_matching_brace(source, start)
        body = source[start:end+1]
        fields = []
        for line in body.split('\n'):
            line = line.strip()
            if line and not line.startswith('//') and not line.startswith('/*') and not line.startswith('*'):
                # Match property declaration: name?: type; or name: type;
                pm = re.match(r'^(?P<name>[a-zA-Z_][a-zA-Z0-9_]*)\??\s*:\s*(?P<type>[^;]+);?', line)
                if pm:
                    fields.append({'name': pm.group('name'), 'type': pm.group('type').strip()})
        items.append({'kind': 'interface', 'name': m.group('name'), 'extends': m.group('extends'), 'fields': fields})
    # Extract classes
    class_pattern = re.compile(
        r'export\s+class\s+(?P<name>[A-Za-z_][A-Za-z0-9_]*)\s*(?:implements\s+(?P<implements>[^{]+))?\s*\{',
        re.MULTILINE
    )
    for m in class_pattern.finditer(source):
        start = m.end() - 1
        end = find_matching_brace(source, start)
        body = source[start:end+1]
        fields = []
        methods = []
        for line in body.split('\n'):
            line = line.strip()
            if not line or line.startswith('//') or line.startswith('/*') or line.startswith('*'):
                continue
            # property
            pm = re.match(r'^(?P<mods>(?:public|private|protected|readonly|static|\s)*)(?P<name>[a-zA-Z_][a-zA-Z0-9_]*)\??\s*:\s*(?P<type>[^;]+);?', line)
            if pm and not re.search(r'\(', pm.group('type')):
                fields.append({'name': pm.group('name'), 'type': pm.group('type').strip(), 'modifiers': pm.group('mods').strip()})
            # method
            mm = re.match(r'^(?P<mods>(?:public|private|protected|static|async|\s)*)(?P<name>[a-zA-Z_][a-zA-Z0-9_]*)\s*\((?P<params>[^)]*)\)\s*(?::\s*(?P<return>[^{]+))?\s*\{?', line)
            if mm:
                methods.append({'name': mm.group('name'), 'params': mm.group('params'), 'return': mm.group('return')})
        items.append({'kind': 'class', 'name': m.group('name'), 'implements': m.group('implements'), 'fields': fields, 'methods': methods})
    return items


def collect_backend():
    groups = defaultdict(list)
    for root, dirs, files in os.walk(BACKEND_ROOT):
        for f in files:
            if f.endswith('.java'):
                path = Path(root) / f
                rel = path.relative_to(BACKEND_ROOT)
                parts = rel.parts
                # Determine module group from first directory after modules/ or shared/
                if 'modules' in parts:
                    idx = parts.index('modules')
                    group = parts[idx+1] if idx+1 < len(parts) else 'other'
                elif 'shared' in parts:
                    group = 'shared'
                else:
                    group = 'other'
                parsed = parse_java_file(path)
                groups[group].append({'path': str(path), 'relative': str(rel), 'declarations': parsed})
    return groups


def collect_frontend():
    domains = defaultdict(list)
    for root, dirs, files in os.walk(FRONTEND_ROOT):
        for f in files:
            if f.endswith(('.ts', '.html', '.css', '.scss')):
                path = Path(root) / f
                rel = path.relative_to(FRONTEND_ROOT)
                parts = rel.parts
                # Determine domain based on path
                if 'domains' in parts:
                    idx = parts.index('domains')
                    domain = parts[idx+1] if idx+1 < len(parts) else 'other'
                elif 'core' in parts:
                    domain = 'core'
                elif 'layout' in parts:
                    domain = 'layout'
                elif 'pages' in parts:
                    domain = 'pages'
                elif 'shared' in parts:
                    domain = 'shared'
                else:
                    domain = 'app'
                info = {'path': str(path), 'relative': str(rel), 'type': f.split('.')[-1]}
                if f.endswith('.ts'):
                    info['declarations'] = parse_ts_interfaces_and_classes(path)
                domains[domain].append(info)
    return domains


def write_markdown(groups, domains):
    lines = []
    lines.append("# Architecture Complète du Projet TANTY ERP")
    lines.append("")
    lines.append("## Vue d'ensemble")
    lines.append("")
    lines.append("Ce document décrit la structure complète du backend Java/Spring Boot (`Api_tanty`) et du frontend Angular (`frontend_erp_tanty`).")
    lines.append("Pour chaque fichier backend, les déclarations (classes, interfaces, enums), champs et méthodes principaux sont extraits.")
    lines.append("")
    lines.append("---")
    lines.append("")
    
    # Backend section
    lines.append("# Backend - Api_tanty")
    lines.append("")
    lines.append(f"**Racine source** : `{BACKEND_ROOT}`")
    lines.append("")
    total_files = sum(len(v) for v in groups.values())
    lines.append(f"**Nombre total de fichiers Java** : {total_files}")
    lines.append("")
    
    # Backend tree by group
    lines.append("## Arborescence du backend (par module)")
    lines.append("")
    for group in sorted(groups.keys()):
        lines.append(f"### Module `{group}`")
        lines.append("")
        for file in sorted(groups[group], key=lambda x: x['relative']):
            lines.append(f"- `{file['relative']}`")
        lines.append("")
    
    # Backend details
    lines.append("## Détails des fichiers backend")
    lines.append("")
    for group in sorted(groups.keys()):
        lines.append(f"### Module `{group}`")
        lines.append("")
        for file in sorted(groups[group], key=lambda x: x['relative']):
            lines.append(f"#### `{file['relative']}`")
            lines.append("")
            if file['declarations']:
                for decl in file['declarations']:
                    kind = decl['kind']
                    name = decl['name']
                    mods = decl['modifiers'] or ''
                    extends = decl['extends']
                    implements = decl['implements']
                    header = f"**{kind}** `{name}`"
                    if mods:
                        header += f" ({mods})"
                    if extends:
                        header += f" extends `{extends}`"
                    if implements:
                        header += f" implements `{implements}`"
                    lines.append(header)
                    lines.append("")
                    if kind == 'enum' and decl['constants']:
                        lines.append("- **Constantes :**")
                        for c in decl['constants']:
                            lines.append(f"  - `{c}`")
                        lines.append("")
                    if kind == 'record' and decl['recordParams']:
                        lines.append("- **Composants (record) :**")
                        for comp in [c.strip() for c in decl['recordParams'].split(',') if c.strip()]:
                            lines.append(f"  - `{comp}`")
                        lines.append("")
                    if decl['fields']:
                        lines.append("- **Champs :**")
                        for f in decl['fields']:
                            annos = f['annotations']
                            mods = f['modifiers']
                            type_ = f['type']
                            names = f['names']
                            prefix = ""
                            if annos:
                                prefix += f"{annos} "
                            if mods:
                                prefix += f"{mods} "
                            lines.append(f"  - `{prefix}{type_} {names}`")
                        lines.append("")
                    if decl['methods']:
                        lines.append("- **Méthodes :**")
                        for m in decl['methods']:
                            annos = m['annotations']
                            mods = m['modifiers']
                            ret = m['return']
                            name = m['name']
                            params = m['params']
                            throws = m['throws']
                            prefix = ""
                            if annos:
                                prefix += f"{annos} "
                            if mods:
                                prefix += f"{mods} "
                            sig = f"`{prefix}{ret} {name}({params})`"
                            if throws:
                                sig += f" throws `{throws}`"
                            lines.append(f"  - {sig}")
                        lines.append("")
            else:
                lines.append("_Fichier vide ou non analysable._")
                lines.append("")
        lines.append("")
    
    # Frontend section
    lines.append("---")
    lines.append("")
    lines.append("# Frontend - frontend_erp_tanty")
    lines.append("")
    lines.append(f"**Racine source** : `{FRONTEND_ROOT}`")
    lines.append("")
    total_front = sum(len(v) for v in domains.values())
    lines.append(f"**Nombre total de fichiers source** : {total_front}")
    lines.append("")
    
    lines.append("## Arborescence du frontend (par domaine)")
    lines.append("")
    for domain in sorted(domains.keys()):
        lines.append(f"### Domaine `{domain}`")
        lines.append("")
        for file in sorted(domains[domain], key=lambda x: x['relative']):
            lines.append(f"- `{file['relative']}` ({file['type']})")
        lines.append("")
    
    lines.append("## Détails des fichiers frontend (TypeScript)")
    lines.append("")
    for domain in sorted(domains.keys()):
        lines.append(f"### Domaine `{domain}`")
        lines.append("")
        for file in sorted(domains[domain], key=lambda x: x['relative']):
            if file.get('declarations'):
                lines.append(f"#### `{file['relative']}`")
                lines.append("")
                for decl in file['declarations']:
                    kind = decl['kind']
                    name = decl['name']
                    ext = decl.get('extends') or decl.get('implements')
                    header = f"**{kind}** `{name}`"
                    if ext:
                        header += f" extends/implements `{ext}`"
                    lines.append(header)
                    lines.append("")
                    if decl.get('fields'):
                        lines.append("- **Propriétés :**")
                        for f in decl['fields']:
                            mods = f.get('modifiers', '')
                            name = f['name']
                            type_ = f['type']
                            lines.append(f"  - `{mods + ' ' if mods else ''}{name}: {type_}`")
                        lines.append("")
                    if decl.get('methods'):
                        lines.append("- **Méthodes :**")
                        for m in decl['methods']:
                            params = m['params']
                            ret = m['return'] or 'void'
                            lines.append(f"  - `{m['name']}({params}): {ret}`")
                        lines.append("")
                lines.append("")
    
    # Interactions / architecture summary
    lines.append("---")
    lines.append("")
    lines.append("# Interactions et architecture")
    lines.append("")
    lines.append("## Backend")
    lines.append("")
    lines.append("- **Architecture hexagonale / DDD** : chaque module est organisé en `domain`, `application`, `infrastructure`, `presentation`.")
    lines.append("- **Domain** : entités, aggregates, value objects, enums, événements, repositories (interfaces), specifications.")
    lines.append("- **Application** : use cases, DTOs, mappers, services applicatifs.")
    lines.append("- **Infrastructure** : entités JPA, repositories Spring Data, implémentations, services techniques (JWT, OTP, stockage).")
    lines.append("- **Presentation** : controllers REST (Spring MVC).")
    lines.append("- **Shared** : composants transverses (audit, exceptions, events, value objects, seeding, web, storage).")
    lines.append("")
    lines.append("## Frontend")
    lines.append("")
    lines.append("- **Framework** : Angular 17+ avec standalone components.")
    lines.append("- **Organisation par domaine métier** sous `src/app/domains/` : commercial, comptable, controle, dg, production, rp, secretaire, stock, admin, users.")
    lines.append("- **Core** : authentification, guards, interceptors HTTP, modèles partagés, services de base (API, audit, documents).")
    lines.append("- **Layout** : composants de mise en page par rôle (commercial-layout, dg-layout, etc.).")
    lines.append("- **Shared** : composants/utilitaires réutilisables (toast, preloader, format utils, mock store).")
    lines.append("- **Stock** : architecture Clean/DDD avec use-cases, domain services, repositories (API + mock).")
    lines.append("")
    lines.append("## Couplage backend / frontend")
    lines.append("")
    lines.append("- Les services Angular (`*Service`) consomment les endpoints REST exposés par les controllers du backend.")
    lines.append("- `AuthService` gère JWT/OTP via `/api/auth/**`.")
    lines.append("- `ProductionService` communique avec les endpoints du module `production` pour les affectations, fiches, sessions de broyage/dosage, rapports.")
    lines.append("- `StockService` et les use-cases du domaine stock utilisent les endpoints du module stock (non présent dans la liste actuelle, mais référencés via `stock.api.ts`).")
    lines.append("")
    
    with open(OUTPUT, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))
    print(f"Fichier généré : {OUTPUT}")
    print(f"Backend files: {total_files}, Frontend files: {total_front}")


if __name__ == "__main__":
    groups = collect_backend()
    domains = collect_frontend()
    write_markdown(groups, domains)
