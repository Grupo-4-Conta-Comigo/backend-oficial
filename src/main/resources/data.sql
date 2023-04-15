insert into restaurante
    (id, nome, cnpj, cep, email, senha)
values
    ('1b1d6bca-7566-44f0-b8d3-08145522c3e2', 'Restaurante Conta Comigo', '70353298000107', '01532030', 'rafael.reis@gmail.com', '$2a$10$0/TKTGxdREbWaWjWYhwf6e9P1fPOAMMNqEnZgOG95jnSkHSfkkIrC');
insert into produto
    (id, nome, categoria, preco, id_restaurante)
values
    ('7f23d51a-e86e-464e-bddd-14cf49bd0414', 'Baião', 'Prato Feito', 23.99, '1b1d6bca-7566-44f0-b8d3-08145522c3e2'),
    ('70f60883-2fdf-4825-9b1d-e81240c4fab8', 'Coxinha', 'Salgado', 4.99, '1b1d6bca-7566-44f0-b8d3-08145522c3e2');