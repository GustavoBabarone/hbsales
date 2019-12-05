CREATE TABLE seg_produtos
(

    id BIGINT           IDENTITY (1, 1)           NOT NULL PRIMARY KEY,
    codigo              BIGINT                    NOT NULL,
    nome                VARCHAR(70)               NOT NULL,
    preco               VARCHAR(70)               NOT NULL,
    id_linha            BIGINT                    NOT NULL,
    unidade_caixa       BIGINT                    NOT NULL,
    peso_unidade        FLOAT                     NOT NULL,
    validade            VARCHAR(10)               NOT NULL,

);

create unique index ix_seg_produto_01 on seg_produtos (codigo asc);