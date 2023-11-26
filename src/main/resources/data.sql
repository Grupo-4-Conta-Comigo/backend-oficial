insert into usuario
    (id, nome, registro, cargo, email, senha, webhookUrl)
values
    ('1b1d6bca-7566-44f0-b8d3-08145522c3e2', 'Restaurante Conta Comigo', '70353298000107', 0, 'conta.comigo@email.com', '$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC', 'https://6557e86bbd4bcef8b6133deb.mockapi.io');
insert into produto
    (nome, categoria, preco, id_restaurante)
values
    ('Baião', 0, 23.99, '1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('Coxinha', 0, 4.99, '1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('Misto Quente', 0, 6.99, '1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('Suco de Laranja', 1, 8.00, '1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('Refrigerante', 1, 7.00, '1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('Água', 1, 4.00, '1b1d6bca-7566-44f0-b8d3-08145522c3e2');
insert into pedido
    (id, mesa, status, data_criacao, id_restaurante)
values
    ('1abcd728-fa91-457f-bb3a-f1d844616e36', 1, 0, current_timestamp,'1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('c3d5701f-9209-4df9-943e-6122b89a762c', 2, 1, current_timestamp,'1b1d6bca-7566-44f0-b8d3-08145522c3e2');
insert into comanda
    (id, nome_dono, status, pedido_id)
values
    ('7862b225-42d2-4fa6-82ce-7bb1acb63595', 'Larissa', 0, '1abcd728-fa91-457f-bb3a-f1d844616e36'),
    ('c68d2d9a-6ab2-4fd1-9257-9b1550a221cb', 'Damasceno', 1, '1abcd728-fa91-457f-bb3a-f1d844616e36'),
    ('54c28c8a-0348-44b1-8f3d-a5f530462716', 'Valentim', 1, 'c3d5701f-9209-4df9-943e-6122b89a762c'),
    ('75f118ec-dc63-4b09-8fd8-91dce4f64b26', 'Pipi', 1, 'c3d5701f-9209-4df9-943e-6122b89a762c');
insert into item_comanda
    (id, observacao, comanda_id, produto_id)
values
    ('7c030d24-b1ef-4aac-8e87-b2bc0828bea6', 'com pimenta', '7862b225-42d2-4fa6-82ce-7bb1acb63595', 1),--Larissa
    ('211de52c-703a-43e4-a695-31107177ca17', null, '7862b225-42d2-4fa6-82ce-7bb1acb63595', 2),--Larissa
    ('116a1cb3-719e-4052-886f-fe547096b5e1', null, 'c68d2d9a-6ab2-4fd1-9257-9b1550a221cb', 2),--Damasceno
    ('c7cc57d9-df75-40db-adb4-742f7dab15b2', 'copo com limão', 'c68d2d9a-6ab2-4fd1-9257-9b1550a221cb',3),--Damasceno
    ('fae38955-7e84-4a85-8469-cbc25a2ea884', null, '54c28c8a-0348-44b1-8f3d-a5f530462716', 4),--Valentim
    ('d67117f0-4256-4d8c-b193-821942c6f49c', null, '75f118ec-dc63-4b09-8fd8-91dce4f64b26', 5), --Pipi
    ('811fc3c9-6a7f-4e23-92ec-9607f761b325', null, '75f118ec-dc63-4b09-8fd8-91dce4f64b26', 6); --Pipi
