# Marketplace Freelancers — Mocks (JSON), Rotas (params) e SQL (INSERT/SELECT)

## 0) Base URL e Headers

- Base URL (exemplo local): `http://localhost:8080`
- Headers comuns:
  - `Content-Type: application/json`
  - `Authorization: Bearer <TOKEN_JWT>`

---

## 1) SQL — Inserts para dados base (skills, categories, subcategories)

As tabelas inferidas pelos `@Table` nos models:
- `categories` (ver [`com.ufc.quixada.api.infrastructure.models.CategoryJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/CategoryJpaModel.java))
- `subcategories` (ver [`com.ufc.quixada.api.infrastructure.models.SubcategoryJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/SubcategoryJpaModel.java))
- `skills` (ver [`com.ufc.quixada.api.infrastructure.models.SkillJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/SkillJpaModel.java))

> Ajuste o schema conforme seu banco (Postgres/MySQL). Os `INSERT`s abaixo são SQL “simples” (ANSI) e funcionam na maioria dos casos.

````sql
-- Inserir categorias
INSERT INTO categories (name) VALUES
  ('Desenvolvimento'),
  ('Design'),
  ('Dados');

-- Inserir skills
INSERT INTO skills (name) VALUES
  ('Java'),
  ('Spring Boot'),
  ('PostgreSQL'),
  ('Docker'),
  ('React'),
  ('Figma');

-- Inserir subcategorias (troque os IDs conforme seu banco retornar)
-- Exemplo assumindo:
-- Desenvolvimento = 1, Design = 2, Dados = 3
INSERT INTO subcategories (name, category_id) VALUES
  ('Backend', 1),
  ('Frontend', 1),
  ('UI/UX', 2),
  ('Branding', 2),
  ('Engenharia de Dados', 3),
  ('BI', 3);
````

### SELECTs imediatos para pegar IDs

````sql
SELECT * FROM categories ORDER BY id;
SELECT * FROM subcategories ORDER BY id;
SELECT * FROM skills ORDER BY id;
````

---

## 2) Rotas / Endpoints (params + exemplos)

### 2.1) Auth

#### POST `/auth/register`
- Body: [`com.ufc.quixada.api.presentation.dtos.RegisterUserDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/RegisterUserDTO.java)
- Cria usuário e (por regra do use case) cria também perfis freelancer/contractor: [`com.ufc.quixada.api.application.usecases.CreateUserUseCase#execute`](src/main/java/com/ufc/quixada/api/application/usecases/CreateUserUseCase.java)

Mock JSON:
````json
// Freelancer
{
  "name": "Gustavo Mock",
  "email": "freelancer@email.com",
  "password": "123"
}
// Contratante
{
  "name": "Juan Mock",
  "email": "contractor@email.com",
  "password": "123"
}
````

cURL:
````bash
curl -i -X POST "http://localhost:8080/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"name":"Gustavo Mock","email":"gustavo.mock@email.com","password":"123456"}'
````

#### POST `/auth/login`
- Body: [`com.ufc.quixada.api.presentation.dtos.LoginDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/LoginDTO.java)
- Response: [`com.ufc.quixada.api.presentation.dtos.TokenDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/TokenDTO.java)

Mock JSON:
````json
{
  "email": "gustavo.mock@email.com",
  "password": "123456"
}
````

cURL:
````bash
curl -s -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"gustavo.mock@email.com","password":"123456"}'
````

---

### 2.2) Projects

Controller: [`com.ufc.quixada.api.presentation.controllers.ProjectController`](src/main/java/com/ufc/quixada/api/presentation/controllers/ProjectController.java)

#### GET `/projects`
- Auth: sim (ver [`com.ufc.quixada.api.config.SecurityConfig`](src/main/java/com/ufc/quixada/api/config/SecurityConfig.java))
- Response: lista de [`com.ufc.quixada.api.presentation.dtos.ProjectResponseDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/ProjectResponseDTO.java)
- Use case: [`com.ufc.quixada.api.application.usecases.GetAllProjects`](src/main/java/com/ufc/quixada/api/application/usecases/GetAllProjects.java)

cURL:
````bash
curl -s "http://localhost:8080/projects" \
  -H "Authorization: Bearer <TOKEN>"
````

#### GET `/projects/{id}`
- Params:
  - `id` (path) = `Long` do projeto
- Use case: [`com.ufc.quixada.api.application.usecases.GetProjectById`](src/main/java/com/ufc/quixada/api/application/usecases/GetProjectById.java)

cURL:
````bash
curl -s "http://localhost:8080/projects/1" \
  -H "Authorization: Bearer <TOKEN>"
````

#### POST `/projects` (consumes `application/json`)
- Body: [`com.ufc.quixada.api.presentation.dtos.CreateProjectJsonDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/CreateProjectJsonDTO.java)
- Regras/validações:
  - `categoryId` deve existir
  - `subcategoryId` deve existir e pertencer à category
  - `skillsIds` deve conter IDs válidos
  - O contractorId vem do token/usuário autenticado (ver controller)
- Use case: [`com.ufc.quixada.api.application.usecases.CreateProject#execute`](src/main/java/com/ufc/quixada/api/application/usecases/CreateProject.java)

Mock JSON (troque IDs pelos do seu banco via SELECT):
````json
{
  "name": "API de Marketplace",
  "description": "Criar endpoints para marketplace de freelancers.",
  "budget": 1500.00,
  "status": "OPEN",
  "experienceLevel": "INTERMEDIATE",
  "deadlineInDays": 15,
  "files": [],
  "isPublic": true,
  "categoryId": 1,
  "subcategoryId": 1,
  "skillsIds": [1, 2, 3]
}
,{
    "name": "Website Institucional",
    "description": "Desenvolver site institucional responsivo.",
    "budget": 800.00,
    "status": "OPEN",
    "experienceLevel": "BEGINNER",
    "deadlineInDays": 10,
    "files": [],
    "isPublic": true,
    "categoryId": 1,
    "subcategoryId": 2,
    "skillsIds": [5, 6]
}
````

cURL:
````bash
curl -s -X POST "http://localhost:8080/projects" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"API de Marketplace",
    "description":"Criar endpoints para marketplace de freelancers.",
    "budget":1500.00,
    "status":"OPEN",
    "experienceLevel":"INTERMEDIATE",
    "deadlineInDays":15,
    "files":[],
    "isPublic":true,
    "categoryId":1,
    "subcategoryId":1,
    "skillsIds":[1,2,3]
  }'
````

> Importante: `status` e `experienceLevel` precisam ser valores válidos dos enums.
> Se você não souber os nomes, abra:
> - [`com.ufc.quixada.api.domain.enums.ProjectStatus`](src/main/java/com/ufc/quixada/api/domain/enums/ProjectStatus.java)
> - [`com.ufc.quixada.api.domain.enums.ExperienceLevel`](src/main/java/com/ufc/quixada/api/domain/enums/ExperienceLevel.java)

---

### 2.3) Proposes (criar e responder)

#### POST `/projects/project/{projectId}/proposes`
- Params:
  - `projectId` (path) = `Long`
- Body: [`com.ufc.quixada.api.presentation.dtos.CreateProposeRequestDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/CreateProposeRequestDTO.java)
- O `freelancerId` vem do usuário autenticado (ver controller).
- Use case: [`com.ufc.quixada.api.application.usecases.CreatePropose#execute`](src/main/java/com/ufc/quixada/api/application/usecases/CreatePropose.java)

Mock JSON (campos dependem do seu DTO; exemplo comum):
````json
{
  "description": "Eu consigo entregar em 10 dias.",
  "duration": 10,
  "price": 1200.00
}
````

cURL:
````bash
curl -s -X POST "http://localhost:8080/projects/project/1/proposes" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"description":"Eu consigo entregar em 10 dias.","duration":10,"price":1200.00}'
````

> O use case define `status = WAITING_ANALYSIS` internamente (ver [`com.ufc.quixada.api.application.usecases.CreatePropose#execute`](src/main/java/com/ufc/quixada/api/application/usecases/CreatePropose.java)).

#### PATCH `/projects/proposes/{proposeId}/answer`
- Params:
  - `proposeId` (path) = `Long`
- Body: [`com.ufc.quixada.api.presentation.dtos.AnswerProposeRequestDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/AnswerProposeRequestDTO.java)
- `newStatus` deve ser uma `String` com o **nome exato** do enum [`com.ufc.quixada.api.domain.enums.ProposeStatus`](src/main/java/com/ufc/quixada/api/domain/enums/ProposeStatus.java)
- Use case: [`com.ufc.quixada.api.application.usecases.AnswerPropose#execute`](src/main/java/com/ufc/quixada/api/application/usecases/AnswerPropose.java)

Mock JSON:
````json
{
  "newStatus": "ACCEPTED"
}
````

cURL:
````bash
curl -i -X PATCH "http://localhost:8080/projects/proposes/1/answer" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"newStatus":"ACCEPTED"}'
````

> Se der erro de enum, abra [`com.ufc.quixada.api.domain.enums.ProposeStatus`](src/main/java/com/ufc/quixada/api/domain/enums/ProposeStatus.java) e use um valor existente.

---

### 2.4) Freelancers

Controller: [`com.ufc.quixada.api.presentation.controllers.FreelancerController`](src/main/java/com/ufc/quixada/api/presentation/controllers/FreelancerController.java)

#### GET `/freelancers`
- Response: lista de [`com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/FreelancerResponseDTO.java)
- Use case: [`com.ufc.quixada.api.application.usecases.GetFreelancers`](src/main/java/com/ufc/quixada/api/application/usecases/GetFreelancers.java)

cURL:
````bash
curl -s "http://localhost:8080/freelancers" \
  -H "Authorization: Bearer <TOKEN>"
````

---

### 2.5) Contractors

Controller existe, mas não tem endpoints implementados ainda:
- [`com.ufc.quixada.api.presentation.controllers.ContractorController`](src/main/java/com/ufc/quixada/api/presentation/controllers/ContractorController.java)

---

## 3) Fluxos completos (para rodar todos os UseCases e endpoints)

### Fluxo A — Setup do banco (pré-requisito)
1) Execute os INSERTs de:
- categories
- subcategories
- skills

2) Pegue IDs:
````sql
SELECT * FROM categories ORDER BY id;
SELECT * FROM subcategories ORDER BY id;
SELECT * FROM skills ORDER BY id;
````

---

### Fluxo B — Usuário → Login → Criar Projeto → Listar → Detalhar

1) Register:
- `POST /auth/register` (JSON de RegisterUserDTO)

2) Login:
- `POST /auth/login` → copie o token

3) Criar projeto:
- `POST /projects` com:
  - `categoryId` existente
  - `subcategoryId` existente e pertencente à category
  - `skillsIds` existentes

4) Listar projetos:
- `GET /projects`

5) Buscar por ID:
- `GET /projects/{id}`

---

### Fluxo C — Criar Proposta → Responder Proposta

1) (Com token) criar propose em um projeto existente:
- `POST /projects/project/{projectId}/proposes`

2) Descobrir `proposeId` no banco (ver SQL abaixo) e responder:
- `PATCH /projects/proposes/{proposeId}/answer`

---

## 4) SQL — SELECTs para todas as entidades/tabelas principais

Tabelas inferidas pelos models:
- `users` (ver [`com.ufc.quixada.api.infrastructure.models.UserJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/UserJpaModel.java))
- `freelancers` (ver [`com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/FreelancerJpaModel.java))
- `contractors` (ver [`com.ufc.quixada.api.infrastructure.models.ContractorJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/ContractorJpaModel.java))
- `projects` (ver [`com.ufc.quixada.api.infrastructure.models.ProjectJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/ProjectJpaModel.java))
- `propose` (ver [`com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity`](src/main/java/com/ufc/quixada/api/infrastructure/models/ProposeJpaEntity.java))
- `files` (ver [`com.ufc.quixada.api.infrastructure.models.FileJpaModel`](src/main/java/com/ufc/quixada/api/infrastructure/models/FileJpaModel.java))
- tabelas de join (pelo mapeamento):
  - `project_skills` (ManyToMany em ProjectJpaModel)
  - `freelancer_projects` (ManyToMany em FreelancerJpaModel)

````sql
-- Users
SELECT id, name, email, password, freelancer_profile_id, contractor_profile_id
FROM users
ORDER BY id;

-- Freelancers
SELECT * FROM freelancers ORDER BY id;

-- Contractors
SELECT * FROM contractors ORDER BY id;

-- Categories / Subcategories / Skills
SELECT * FROM categories ORDER BY id;
SELECT * FROM subcategories ORDER BY id;
SELECT * FROM skills ORDER BY id;

-- Projects
SELECT * FROM projects ORDER BY id;

-- Proposes
SELECT * FROM propose ORDER BY id;

-- Files
SELECT * FROM files ORDER BY id;

-- Joins
SELECT * FROM project_skills ORDER BY project_id, skill_id;
SELECT * FROM freelancer_projects ORDER BY freelancer_id, project_id;
````

### SELECTs úteis para depuração dos fluxos

````sql
-- Ver projetos com categoria/subcategoria (join simples)
SELECT
  p.id AS project_id,
  p.name AS project_name,
  c.id AS category_id,
  c.name AS category_name,
  s.id AS subcategory_id,
  s.name AS subcategory_name
FROM projects p
JOIN categories c ON c.id = p.category_id
JOIN subcategories s ON s.id = p.subcategory_id
ORDER BY p.id;

-- Ver proposes com projeto e freelancer
SELECT
  pr.id AS propose_id,
  pr.status,
  pr.price,
  pr.duration,
  pr.project_id,
  pr.freelancer_id
FROM propose pr
ORDER BY pr.id;
````

---

## 5) Observações sobre “files” no CreateProjectJsonDTO

O [`com.ufc.quixada.api.presentation.dtos.CreateProjectJsonDTO`](src/main/java/com/ufc/quixada/api/presentation/dtos/CreateProjectJsonDTO.java) tem `files: List<String>` e o mapper usa [`com.ufc.quixada.api.application.mappers.ProjectMapper#mockFilesDTOToDomain`](src/main/java/com/ufc/quixada/api/application/mappers/ProjectMapper.java) para criar arquivos “mock” com `id = null`.

No use case [`com.ufc.quixada.api.application.usecases.CreateProject#execute`](src/main/java/com/ufc/quixada/api/application/usecases/CreateProject.java), os `fileIds` são filtrados por `Objects::nonNull`, então **não exige inserts** em `files` para criar projeto via JSON.

---

## 6) Checklist rápido (para você “rodar tudo”)

1) SQL: inserir categories, subcategories, skills ✅  
2) `POST /auth/register` ✅  
3) `POST /auth/login` ✅  
4) `POST /projects` ✅  
5) `GET /projects` ✅  
6) `GET /projects/{id}` ✅  
7) `POST /projects/project/{projectId}/proposes` ✅  
8) SQL: pegar proposeId (`SELECT * FROM propose`) ✅  
9) `PATCH /projects/proposes/{proposeId}/answer` ✅  
10) `GET /freelancers` ✅  

---
