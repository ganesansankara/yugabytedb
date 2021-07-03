#!/usr/bin/env python

import pika

connection = pika.BlockingConnection(pika.ConnectionParameters(host='192.168.1.122', 
port=5672,
credentials=pika.credentials.PlainCredentials(
        'ganesan', 'password')))
        
channel = connection.channel()

channel.exchange_declare(exchange='bucketevents',
                         exchange_type='fanout')


result = channel.queue_declare(queue='ganesan',exclusive=False)
queue_name = result.method.queue
print (f'queuename[{queue_name}]')

channel.queue_bind(exchange='bucketevents',
                   queue=queue_name)

print(' [*] Waiting for logs. To exit press CTRL+C')

def callback(ch, method, properties, body):
    print(" [x] %r" % body)

channel.basic_consume(queue=queue_name,
                    on_message_callback=callback,  
                    auto_ack=False)

channel.start_consuming()