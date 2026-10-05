package com.NTFOODS.Api_tanty.shared.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_TANTY = "tanty.events";

    public static final String QUEUE_STOCK_SEUIL = "stock.tampon.seuilAtteint";
    public static final String QUEUE_DOTATION_APPROUVEE = "stock.dotation.approuvee";
    public static final String QUEUE_LOT_DECLARE = "production.lot.declare";
    public static final String QUEUE_LOT_VALIDE_STOCK = "production.lot.valideParStock";
    public static final String QUEUE_VERSEMENT_ALERTE = "comptabilite.versement.alerteRouge";
    public static final String QUEUE_PRECOMMANDE_VALIDEE = "commercial.precommande.validee";

    @Bean
    public TopicExchange tantyExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TANTY).durable(true).build();
    }

    @Bean Queue queueSeuilStock() { return QueueBuilder.durable(QUEUE_STOCK_SEUIL).build(); }
    @Bean Queue queueDotationApprouvee() { return QueueBuilder.durable(QUEUE_DOTATION_APPROUVEE).build(); }
    @Bean Queue queueLotDeclare() { return QueueBuilder.durable(QUEUE_LOT_DECLARE).build(); }
    @Bean Queue queueLotValideStock() { return QueueBuilder.durable(QUEUE_LOT_VALIDE_STOCK).build(); }
    @Bean Queue queueVersementAlerte() { return QueueBuilder.durable(QUEUE_VERSEMENT_ALERTE).build(); }
    @Bean Queue queuePreCommandeValidee() { return QueueBuilder.durable(QUEUE_PRECOMMANDE_VALIDEE).build(); }

    @Bean Binding bindingSeuilStock(Queue queueSeuilStock, TopicExchange tantyExchange) {
        return BindingBuilder.bind(queueSeuilStock).to(tantyExchange).with("stock.tampon.#");
    }
    @Bean Binding bindingDotation(Queue queueDotationApprouvee, TopicExchange tantyExchange) {
        return BindingBuilder.bind(queueDotationApprouvee).to(tantyExchange).with("stock.dotation.#");
    }
    @Bean Binding bindingLotDeclare(Queue queueLotDeclare, TopicExchange tantyExchange) {
        return BindingBuilder.bind(queueLotDeclare).to(tantyExchange).with("production.lot.declare");
    }
    @Bean Binding bindingLotValide(Queue queueLotValideStock, TopicExchange tantyExchange) {
        return BindingBuilder.bind(queueLotValideStock).to(tantyExchange).with("production.lot.valide.#");
    }
    @Bean Binding bindingVersementAlerte(Queue queueVersementAlerte, TopicExchange tantyExchange) {
        return BindingBuilder.bind(queueVersementAlerte).to(tantyExchange).with("comptabilite.versement.alerte.#");
    }
    @Bean Binding bindingPreCommande(Queue queuePreCommandeValidee, TopicExchange tantyExchange) {
        return BindingBuilder.bind(queuePreCommandeValidee).to(tantyExchange).with("commercial.precommande.#");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
