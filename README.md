# Spring Boot API de Autenticação

Este repositório contém uma API de autenticação de usuários desenvolvida com Spring Boot, utilizando JSON Web Token (JWT) para segurança e controle de acesso.

## Tecnologias Utilizadas

- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **H2 Database** (Banco de dados em memória para testes)
- **Lombok** (Redução de boilerplate code)
- **JSON Web Token (JWT)** (para autenticação)
- **Spring Boot DevTools** (Hot reload para desenvolvimento)

## Configuração do Projeto

### Requisitos
- **JDK 17+**
- **Maven**

### Como Rodar

1. Clone o repositório:
   ```sh
   git clone https://github.com/Guh-paixao/auth-login-api.git
   cd auth-login-api
   ```

2. Compile e execute o projeto:
   ```sh
   mvn spring-boot:run
   ```

3. A API estará disponível em:
   ```
   http://localhost:8080
   ```

## Endpoints Principais

### Autenticação
- `POST /auth/login` - Autentica um usuário e retorna um token JWT
- `POST /auth/register` - Registra um novo usuário

### Protegidos (Necessário Token JWT)
- `GET /users` - Retorna informações sobre os usuários cadastrados

## Testes
Para executar os testes, utilize o comando:
```sh
mvn test
```

## Contribuição
Pull requests são bem-vindos! Para grandes alterações, abra uma issue primeiro para discutirmos o que você gostaria de modificar.

## Licença
Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
Desenvolvido por [Gustavo Paixão]([https://github.com/Guh-paixao]).

