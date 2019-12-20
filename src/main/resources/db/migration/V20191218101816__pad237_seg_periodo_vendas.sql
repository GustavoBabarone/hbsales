CREATE TABLE seg_periodo_vendas
(
    id              BIGINT IDENTITY(1,1)                    NOT NULL,
    data_inicio     VARCHAR(10)                             NOT NULL,
    data_fim        VARCHAR(10)                             NOT NULL,
    id_fornecedor   BIGINT REFERENCES seg_fornecedores(id)  NOT NULL,
    data_retirada   VARCHAR(10)                             NOT NULL,
    descricao       VARCHAR(50)                             NOT NULL
);

CREATE UNIQUE index ix_seg_periodo_vendas_01 ON seg_periodo_vendas (id asc);