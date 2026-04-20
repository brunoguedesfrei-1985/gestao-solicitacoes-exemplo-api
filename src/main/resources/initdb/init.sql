-- init.sql: Exemplo de script de inicialização para PostgreSQL
INSERT INTO cargo (id, nome) VALUES (1, 'gerente') ON CONFLICT DO NOTHING;
INSERT INTO cargo (id, nome) VALUES (2, 'analista') ON CONFLICT DO NOTHING;
INSERT INTO cargo (id, nome) VALUES (3, 'tecnico') ON CONFLICT DO NOTHING;

INSERT INTO usuario
(id, cpf, nome, email, senha, cargo_id)
VALUES(1, '77689062768', 'sicrano alves', 'sicrano@gmail.com', '$2a$12$6R29ssoUy0DpUtVaDgIPDu94qyPldM/P9wUn7CWBFJTmKSEolv2OW', 2) ON CONFLICT DO NOTHING;
INSERT INTO usuario
(id, cpf, nome, email, senha, cargo_id)
VALUES(2, '01182101062', 'fulano silva', 'fulano@gmail.com', '$2a$12$AqU9he2G4kUJvZ6VogddcevD2Yyu4AAiVTT4pNRKQzwtWTSr88sdy', 2) ON CONFLICT DO NOTHING;


INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(1, 'Solicitacao de TI', 'teste 1', 'TI', 'ABERTA', '2026-04-20 19:49:03.709', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(2, 'Solicitacao de TI', 'teste 2', 'TI', 'ABERTA', '2026-04-20 19:49:07.891', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(3, 'Solicitacao de TI', 'teste 3', 'TI', 'ABERTA', '2026-04-20 19:49:10.987', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(4, 'Solicitacao de TI', 'teste 4', 'TI', 'ABERTA', '2026-04-20 19:49:14.062', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(5, 'Solicitacao de TI', 'teste 5', 'TI', 'ABERTA', '2026-04-20 19:49:17.827', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(6, 'Solicitacao de TI', 'teste 6', 'TI', 'ABERTA', '2026-04-20 19:49:21.512', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(7, 'Solicitacao de TI', 'teste 7', 'TI', 'ABERTA', '2026-04-20 19:49:25.362', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(8, 'Solicitacao de TI', 'teste 8', 'TI', 'ABERTA', '2026-04-20 19:49:29.906', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(9, 'Solicitacao de TI', 'teste 9', 'TI', 'ABERTA', '2026-04-20 19:49:33.803', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(10, 'Solicitacao de TI', 'teste 10', 'TI', 'ABERTA', '2026-04-20 19:49:38.504', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(11, 'Solicitacao de TI', 'teste 11', 'TI', 'ABERTA', '2026-04-20 19:49:42.120', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(12, 'Solicitacao de TI', 'teste 12', 'TI', 'ABERTA', '2026-04-20 19:49:45.423', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(13, 'Solicitacao de TI', 'teste 13', 'TI', 'ABERTA', '2026-04-20 19:49:48.742', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(14, 'Solicitacao de TI', 'teste 14', 'TI', 'ABERTA', '2026-04-20 19:49:51.966', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(15, 'Solicitacao de TI', 'teste 15', 'TI', 'ABERTA', '2026-04-20 19:49:57.569', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(16, 'Solicitacao de TI', 'teste 16', 'TI', 'ABERTA', '2026-04-20 19:50:01.188', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(17, 'Solicitacao de TI', 'teste 17', 'TI', 'ABERTA', '2026-04-20 19:50:04.588', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(18, 'Solicitacao de TI', 'teste 18', 'TI', 'ABERTA', '2026-04-20 19:50:07.848', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(19, 'Solicitacao de TI', 'teste 19', 'TI', 'ABERTA', '2026-04-20 19:50:11.586', 1, 2) ON CONFLICT DO NOTHING;
INSERT INTO solicitacao
(id, titulo, descricao, categoria, status, data_criacao, usuario_atribuido_id, usuario_atendente_id)
VALUES(20, 'Solicitacao de TI', 'teste 20', 'TI', 'ABERTA', '2026-04-20 19:50:15.821', 1, 2) ON CONFLICT DO NOTHING;