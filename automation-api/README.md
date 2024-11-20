# Documentação do Ambiente de Teste para Automação de Testes de API

## 1. Introdução
Este documento fornece instruções sobre como executar os testes de API do projeto utilizando Java, IntelliJ IDEA, Maven, Rest Assured e JUnit5. Ele também cobre como gerar relatórios do Allure após a execução dos testes.

## 2. Requisitos do Sistema
Antes de executar os testes, verifique se você possui os seguintes itens instalados:
- **Java Development Kit (JDK)**: JDK 11 ou superior.
- **IDE**: IntelliJ IDEA (Community ou Ultimate Edition).
- **Maven**: Maven 3.6.0 ou superior.

## 3. Executando os Testes

### 3.1. Executar Testes no IntelliJ
1. **Abrir o Projeto**: Abra o projeto no IntelliJ IDEA.
2. **Executar Testes**:
   - Clique com o botão direito na classe de teste ou no método de teste e selecione "Run".

### 3.2. Executar Testes via Linha de Comando
Para executar os testes via linha de comando, navegue até a raiz do seu projeto e use o seguinte comando para executar todos os testes:

   - mvn clean test
### 3.3. Executar uma Classe de Testes
Para executar uma classe de testes, você pode usar o seguinte comando:

   mvn -Dtest=NomeDaClasseDeTeste clean test
   Exemplo: mvn -Dtest=GetAllBreedsTest clean test

### 3.4. Executar um Teste Específico
   Para executar um teste específico, você pode usar o seguinte comando:

   mvn -Dtest=NomeDaClasseDeTeste#nomeDoMetodoDeTeste clean test
   Exemplo: mvn -Dtest=GetAllBreedsTest#testGetAllBreeds clean test

## 4. Gerando Relatórios do Allure
   Após a execução dos testes, você pode gerar os relatórios do Allure com o seguinte comando:

   mvn allure:report

   Os relatórios gerados estarão localizados em target/site/allure-maven. Para visualizar o relatório, você pode abrir o arquivo index.html no navegador nesse diretório.