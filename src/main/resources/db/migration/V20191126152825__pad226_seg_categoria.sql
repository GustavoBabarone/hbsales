create table seg_categorias
(
    id BIGINT IDENTITY (1, 1)                            NOT NULL PRIMARY KEY,
    codigo VARCHAR(14)                                   NOT NULL,
    id_fornecedor BIGINT REFERENCES seg_fornecedores(id) NOT NULL,
    nome VARCHAR(70)                                     NOT NULL

);

create unique index ix_seg_categorias_01 on seg_categorias (codigo asc);
create unique index ix_seg_categorias_03 on seg_categorias (nome asc);

