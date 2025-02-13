package com.inkblogdb.commons.db;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlBuilderTest {

    @Nested
    class toSelectSQL {
        @Test
        void 全検索のSELECT文が生成できること() {
            // when
            var sql = new SqlBuilder()
                .table("test_table")
                .toSelectSQL();

            // then
            assertEquals("SELECT * FROM `test_table`;", sql);
        }

        @Test
        void カラム指定のSELECT文が生成できること() {
            // when
            var sql = new SqlBuilder()
                .table("test_table")
                .column("id")
                .toSelectSQL();

            // then
            assertEquals("SELECT `id` FROM `test_table`;", sql);
        }

        @Nested
        class where {
            @Test
            void 数値が一致する条件のSELECT文が生成できること1() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .where("id", 1)
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` = 1;", sql);
            }

            @Test
            void 文字列が一致する条件のSELECT文が生成できること1() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .where("id", "A")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` = 'A';", sql);
            }

            @Test
            void 数値が一致する条件のSELECT文が生成できること2() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .whereEqual("id", 1)
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` = 1;", sql);
            }

            @Test
            void 文字列が一致する条件のSELECT文が生成できること2() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .whereEqual("id", "A")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` = 'A';", sql);
            }

            @Test
            void 数値が一致しない条件のSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .whereNot("id", 1)
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` <> 1;", sql);
            }

            @Test
            void 文字列が一致しない条件のSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .whereNot("id", "A")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` <> 'A';", sql);
            }

            @Test
            void 演算子を指定した条件のSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .where("id", "=", "A")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` = 'A';", sql);
            }

            @Test
            void 条件に含むSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .whereIn("id", "A", "B", "C")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` IN ('A', 'B', 'C');", sql);
            }

            @Test
            void 条件に含まれないSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .whereNotIn("id", "A", "B", "C")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` WHERE `id` NOT IN ('A', 'B', 'C');", sql);
            }
        }

        @Nested
        class orderBy {
            @Test
            void 順序を指定してSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .orderBy("id")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` ORDER BY `id` ASC;", sql);
            }

            @Test
            void 昇順のSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .orderByAsc("id")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` ORDER BY `id` ASC;", sql);
            }

            @Test
            void 降順のSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table")
                    .orderByDesc("id")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table` ORDER BY `id` DESC;", sql);
            }
        }

        @Nested
        class join {
            @Test
            void JOINのSELECT文が生成できること() {
                // when
                var sql = new SqlBuilder()
                    .table("test_table1")
                    .join("test_table2", "test_table1", "test_id", "test_table2", "id")
                    .toSelectSQL();

                // then
                assertEquals("SELECT * FROM `test_table1` JOIN `test_table2` ON `test_table1`.`test_id` = `test_table2`.`id`;", sql);
            }
        }
    }

}
