# FACETEC - Interface FTCA-888

Aplicação para gerenciar aparelhos de reconhecimento facial, composto por uma interface Web e outra em Desktop. 

A interface Web desenvolvida em <a href="https://angular.io/">Angular</a> será disponibilizada na cloud ou em servidor local para gerenciar os dados que vão para os aparelhos.

A aplicação Desktop deverá ser instalada em um servidor local, na mesma rede em que foram instalados os aparelhos. Esta aplicação é responsável por fazer a integração dos dados cadastrados na interface Web com os aparelhos.


## Pré-requisito

* JDK: 1.8 ou superior
* Maven: 3.3.9 ou superior
* PostgreSQL: 9.4.17 ou superior


## Desenvolvimento
Rodar o build da aplicação utilizando maven. Na raíz do projeto facetec, executar o comando `mvn clean install`.

### Interface WEB

1. Criar uma base em PostgreSQL, as configurações do banco se encontram em `facetec-core\src\test\resources\application.properties`. É possível também subir a aplicação utilizando base H2, fazendo a configuração neste mesmo arquivo.

2. Executar a classe `StartFaceTecApplication`. De acordo com a configuração em `application.properties` a aplicação vai subir em `http://localhost:8090/ftca888`.
* Para criar o primeiro usuário na base, pode se usar os inserts abaixo para criar usuários facetec e admin com senha `facetec`, para a localidade `facetec`. A senha é armazenada no banco criptografada pela classe `BCryptPasswordEncoder`.
    * `INSERT INTO LOCALIDADE_USUARIO VALUES (1, 'Facetec');`
    * `INSERT INTO FACETEC_USER VALUES (1, '$2a$11$Xr/V./QVjIbjSC0Q5FIMre7f0PNdJFwBDUHv80DJW/5nBmyLl7uR.', true, 'admin', 1);`
    * `INSERT INTO FACETEC_USER VALUES (2, '$2a$11$Xr/V./QVjIbjSC0Q5FIMre7f0PNdJFwBDUHv80DJW/5nBmyLl7uR.', false, 'facetec', 1);`

3. A criação das tabelas é feita utilizando <a href="https://www.liquibase.org/">Liquibase</a> na subida da aplicação, com base no arquivo `db.changelog.yaml`;

4. Para desenvolvimento das telas, pode se executar o comando `ng serve` em `facetec-ui\src\main\web\`, a interface vai subir na porta 4200 para desenvolvimento. Ao rodar o build as telas serão compiladas e incluídas dentro de `facetec-core`.

5. Existem alguns paramêtros do sistema que podem ser definidos em `application.properties`:
* `facetec.admin.password` define a senha para salvar os aparelhos no menu Setup.
* `facetec.exclusao.visitante.cron` define uma cron com a periodicidade em que os visitantes devem ser excluídos da base (padrão para executar todo dia a meia noite). O parâmetro `facetec.exclusao.visitante.dias` define a quantidade de dias em que o visitante deve ficar na base (padrão para 15). 

### Aplicação Desktop

1. Executar a classe `ClientApplication`.

2. Parâmetros configuráveis que podem ser definidos em `facetec-client\src\main\resources\application.properties`:
* `facetec.client.url` define a URL do servidor em que está executando a interface WEB (default https://www.facetec.tk/ftca888/);

## Deploy

1. Para interface WEB, subir o artefato `facetec-core-<version>.war` utilizando o comando abaixo para gerar log da aplicação em arquivo:

`java -jar facetec-core-<version>.war -Dspring.config.location=application.properties > facetec.log 2>&1 &`

* Domínio `facetec.tk` registrado no <a href="https://my.freenom.com">freenom</a>.
* Certificado digital gerado utilizando <a href=https://certbot.eff.org/>certbot</a>.

2. Para gerar a aplicação Desktop como arquivo executável, executar o comando abaixo após o build na pasta `facetec-client\target`, trocando os campos `<version>` para a versão que será gerada e `<Path-do-projeto>` para o diretório onde se encontra o projeto:

`javapackager -deploy -native exe -outdir ./executable -srcfiles facetec-client-<version>.jar -outfile instalador-ftca888 -name instalador-ftca888 -title "FACETEC - Instalador FTCA-888" -appclass org.springframework.boot.loader.JarLauncher -v -Bicon=<Path-do-projeto>\facetec\facetec-client\src\main\resources\facetec_logo.ico -BappVersion=<version>`
