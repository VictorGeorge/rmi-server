# rmi-server
Distributed System

# Sobre o trabalho
Arquitetura Cliente-Servidor, Modelo Publisher/Subscriber (Eventos e
Notificações), Java RMI e Controle de Concorrência.

# Instruções
Desenvolver um sistema cliente-servidor para uma agência de turismo.
Utilizar a middleware Java RMI (Remote Invocation Method) para prover
a comunicação entre os clientes e o servidor da agência de turismo.

Requisitos da aplicação:

• Utilizar a middleware Java RMI (Remote Invocation Method) para prover
a comunicação entre os clientes e o servidor da agência de turismo.

Métodos disponíveis no servidor (valor 2,0):

• Consulta e compra de passagens aéreas. Serão fornecidos os
seguintes dados: “somente ida” ou “ida e volta”, origem, destino, data da
ida, data da volta e número de pessoas (valor 0,4);

• Consulta e compra de hospedagem. Serão fornecidos os seguintes
dados: destino (nome da cidade ou do hotel), data da entrada e data da
saída, número de quartos e número de pessoas (valor 0,4);

• Consulta e compra de pacotes (passagem aérea + hospedagem).
Serão fornecidos os dados acima (valor 0,4).

• Registro de interesse em eventos: o processo servidor tem a tarefa de
permitir que cada cliente registre interesse em um ou mais eventos
(novos voos, novas vagas em hotéis, novos pacotes). No momento do
registro, o cliente deve informar o(s) evento(s) desejado(s), o destino
desejado, um preço máximo que ele deseja pagar e sua referência de
objeto remoto (valor 0,4).

• Remover registro de interesse (valor 0,1).
• Observação sobre o evento pacote: se um cliente registrar interesse
em pacote para um destino X e surgir apenas um voo, mas não uma
hospedagem (ou vice-versa), o servidor não enviará uma notificação ao
cliente. A notificação será enviada apenas se um pacote completo for
encontrado (pattern of events). Servidor mantém registro de um evento
e espera o próximo ocorrer para enviar a notificação.

Implementar um mecanismo que controle o acesso concorrente aos métodos
do servidor (valor 0,3).

Método disponível no cliente (valor 0,5):

• Notificação de evento: o processo servidor tem a tarefa de enviar, via
chamada de método, notificações assíncronas de eventos aos clientes
interessados.
