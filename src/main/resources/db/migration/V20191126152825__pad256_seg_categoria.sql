create table seg_categorias
(
    id BIGINT IDENTITY (1, 1)       NOT NULL,
    nome_categoria VARCHAR(70)      NOT NULL,
    id_fornecedor_categoria BIGINT  NOT NULL
);

create unique index ix_seg_categorias_01 on seg_categorias (nome_categoria asc);
create unique index ix_seg_categorias_02 on seg_categorias (id_fornecedor_categoria asc);
