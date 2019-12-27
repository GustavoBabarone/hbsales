CREATE TABLE seg_pedidos
(
    id              BIGINT IDENTITY(1, 1)                   NOT NULL,
    codigo_pedido   VARCHAR(10)                             NOT NULL,
    id_funcionario  BIGINT REFERENCES seg_funcionarios (id) NOT NULL,
    id_fornecedor   BIGINT REFERENCES seg_fornecedores (id) NOT NULL,
    preco_total     DECIMAL(25, 2)                          NOT NULL,
    status          VARCHAR                                 NOT NULL,
    data_registro   DATE                                    NOT NULL
);