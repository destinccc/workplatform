package com.uuc.alarm.vm.client.builder;

public enum QueryBuilderType {
    RangeQuery {
        @SuppressWarnings("unchecked")
        public RangeQueryBuilder newInstance(String prometheusUrl) {
            return new RangeQueryBuilder(prometheusUrl);
        }
    },

    InstantQuery {
        @SuppressWarnings("unchecked")
        public InstantQueryBuilder newInstance(String prometheusUrl) {
            return new InstantQueryBuilder(prometheusUrl);
        }

    },

    SeriesMetadaQuery {
        @SuppressWarnings("unchecked")
        public QueryBuilder newInstance(String prometheusUrl) {
            return new SeriesMetaQueryBuilder(prometheusUrl);
        }
    },

    LabelMetadaQuery {
        @SuppressWarnings("unchecked")
        public QueryBuilder newInstance(String prometheusUrl) {
            return new LabelMetaQueryBuilder(prometheusUrl);
        }
    },

    TargetMetadaQuery {
        @SuppressWarnings("unchecked")
        public QueryBuilder newInstance(String prometheusUrl) {
            return new TargetMetaQueryBuilder(prometheusUrl);
        }
    },

    AlertManagerMetadaQuery {
        @SuppressWarnings("unchecked")
        public QueryBuilder newInstance(String prometheusUrl) {
            return new AlertManagerMetaQueryBuilder(prometheusUrl);
        }
    },

    StatusMetadaQuery {
        @SuppressWarnings("unchecked")
        public QueryBuilder newInstance(String prometheusUrl) {
            return new StatusMetaQueryBuilder(prometheusUrl);
        }
    };


    public abstract <T extends QueryBuilder> T newInstance(String prometheusUrl);
}
