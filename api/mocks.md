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

# Mocks e Exemplos de Uso da API
````json
// Freelancers
[{
  "name": "Gustavo Mock",
  "email": "freelancer@email.com",
  "password": "12345678"
},
{
  "name": "Gustavo Mock 2",
  "email": "freelancer2@email.com",
  "password": "12345678"
},
// Contratante
{
  "name": "Juan Mock",
  "email": "contractor@email.com",
  "password": "12345678"
}
]
````

## 1) Autenticação (Register e Login)
Mock JSON:
````json
{
  "email": "gustavo.mock@email.com",
  "password": "123456"
}
````

## 2) Projects

Mock JSON (troque IDs pelos do seu banco via SELECT):
````json
[
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
    },
    {
        "name": "Website Institucional PRIVADO",
        "description": "Desenvolver site institucional responsivo.",
        "budget": 1800.00,
        "status": "OPEN",
        "experienceLevel": "BEGINNER",
        "deadlineInDays": 10,
        "files": [],
        "isPublic": false,
        "categoryId": 1,
        "subcategoryId": 2,
        "skillsIds": [5, 6]
    }
]
````

### 2.1) Projects Controller

Mock JSON (campos dependem do seu DTO; exemplo comum):
````json
{
  "description": "Eu consigo entregar em 10 dias.",
  "duration": 10,
  "price": 1200.00
}
````

### 2.3) Answer Propose

Mock JSON:
````json
{
  "newStatus": "ACCEPTED"
}
````

## 3) Fluxos de Uso da API

````sql
SELECT * FROM categories ORDER BY id;
SELECT * FROM subcategories ORDER BY id;
SELECT * FROM skills ORDER BY id;
````

---

## 4) SELECTs para verificar dados inseridos

### SELECTs úteis para depuração dos fluxos

```sql
-- Users/Freelancers/Contractors
SELECT u.id as user_id, u.email, u.name,u.password, f.id as freelancer_profile_id, c.id as contractor_profile_id  
FROM users u join freelancers f on u.id = f.user_id 
			 join contractors c on u.id = c.user_id
ORDER BY u.id;

-- Categories / Subcategories / Skills
SELECT * FROM categories ORDER BY id;
SELECT * FROM subcategories ORDER BY id;
SELECT * FROM skills ORDER BY id;

SELECT c.id as category_id, c.name as category_name, s.id as subcategory_id, s.name as subcategory_name
	from public.categories c 
	join public.subcategories s on s.category_id = c.id

-- Projects
SELECT * FROM projects ORDER BY id;

-- Proposes
SELECT * FROM proposes ORDER BY id;

-- Files
SELECT * FROM files ORDER BY id;

-- Joins
SELECT * FROM project_skills ORDER BY project_id, skill_id;
SELECT * FROM project_freelancers ORDER BY freelancer_id, project_id;
SELECT * from projects ORDER BY contractor_id
```

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