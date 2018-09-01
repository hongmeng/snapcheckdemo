# SnapCheck Demo

## Setup Ethereum on AWS

This demo requires a private Ethereum node to run. Although it should work on
any public Ethereum nodes in theory, it is not tested as such for performance
(or anything else as a matter of fact). 

One can choose to use AWS Ethereum template to bypass the manual setup steps below.

For manual setup, AWS Ubuntu instance is used, and below are the steps:

** As root user **

>apt-get update
>apt-get install docker.io
>add-apt-repository -y ppa:ethereum/ethereum
>apt-get update
>apt-get install -y ethereum
>docker run -d --name ethereum -p 8545:8545 -p 30303:30303 ethereum/client-go --rpc --rpcaddr "0.0.0.0" --rpcapi="db,eth,net,web3,personal" --rpccorsdomain "*" --dev

Verify ethereum is up and running with
>curl -X POST --header 'Content-Type: application/json'  --data '{"jsonrpc":"2.0","method":"eth_accounts","params":[],"id":1}'  http://127.0.0.1:8545

## Setup Accounts on Ethereum
Run geth using the following command

>docker exec -it ethereum geth attach ipc:/tmp/geth.ipc

From geth, type
>web3.personal.unlockAccount(eth.accounts[0], "");
>web3.personal.newAccount('123456');
>web3.personal.unlockAccount(eth.accounts[1], "123456");
>web3.eth.accounts

## Deploying and running
To run the code as a service, deploy the artifact (manually) and execute
>nohup java -jar demo-0.0.1-SNAPSHOT.jar > log.txt &

You can verify the service at
http://<IP>:8080/transactions/list

Or POST the following to http://<IP>:8080/transactions/create

>{
>	"accountFrom": "0xd9c2f96afcd5e07587b5612f4931157b62af0dde",
>	"accountTo": "0x4e4b30a9c118aefcd182ade319d4eca5ab0dcb1e",
>	"from": "John",
>	"to": "Jane",
>	"routingNumber": "123456",
>	"accountNumber": "987654",
>	"amount": 1.00,
>	"attachment": "f1f2f3f4f5"
>}

