# Marketplace Freelancers

O **Marketplace Freelancers** é uma API REST desenvolvida em Java com Spring Boot que conecta freelancers e contratantes, permitindo a criação de projetos, envio e gestão de propostas e gerenciamento de relações profissionais de maneira eficiente e segura.

## Visão Geral

O sistema implementa um marketplace para contratação de freelancers, estruturando todas as operações em torno das seguintes entidades centrais:
- **Usuário (User):** Um usuário pode atuar como freelancer, contratante (contractor) ou ambos no sistema.
- **Freelancer:** Usuários que desejam realizar projetos.
- **Contratante (Contractor):** Usuários que cadastram projetos e contratam freelancers.
- **Projeto (Project):** Criado pelo contratante, cada projeto possui descrição, orçamento, status, prazo, categoria, arquivos e skills associadas.
- **Proposta (Propose):** Submetida por freelancers para projetos específicos, podendo ser aceita ou recusada pelo contratante.

## Principais Funcionalidades

- **Gestão de Usuários:** Cadastro de usuários com perfis automáticos de freelancer e contratante e autenticação via JWT.
- **Gestão de Projetos:** Criação, consulta e detalhamento de projetos, com associação de habilidades (skills), categorias e envio de arquivos.
- **Gestão de Propostas:** Envio de propostas por freelancers, com aceite ou recusa por parte dos contratantes. Propostas aceitas vinculam o freelancer ao projeto.
- **Visibilidade Inteligente:** Listagem e filtragem automática de projetos baseada em regras de visibilidade e controle de acesso granular.
- **Camadas bem definidas (Clean Architecture):** Separação clara entre entidades de domínio, casos de uso, infraestrutura e apresentação, garantindo fácil manutenção e evolução do sistema.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 4.0.1
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Lombok, MapStruct e outras ferramentas para produtividade e documentação

## Começando

> As instruções para rodar o projeto, configurações de banco e exemplos de uso da API deverão ser incluídas aqui conforme evolução da documentação.

---

Este repositório representa uma base sólida para um marketplace de freelancers moderno e seguro. Para detalhes arquiteturais e diagramas, consulte também o arquivo [ARQUITETURA.md](./ARQUITETURA.md).
