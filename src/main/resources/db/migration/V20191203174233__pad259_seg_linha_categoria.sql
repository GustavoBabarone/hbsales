create table seg_linha_categoria
(
    id BIGINT IDENTITY (1, 1)                      PRIMARY KEY NOT NULL,
    codigo_linha BIGINT                                        NOT NULL,
    id_categoria BIGINT REFERENCES seg_categorias(id)          NOT NULL,
    nome VARCHAR(60)                                           NOT NULL
);

create unique index ix_seg_seg_linha_categoria_01 on seg_linha_categoria (id asc);
create unique index ix_seg_seg_linha_categoria_02 on seg_linha_categoria (codigo_linha asc);
