#!/bin/bash
set -e

echo "Enabling RabbitMQ delayed message plugin..."
rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

# Start RabbitMQ normally
exec rabbitmq-server "$@"
