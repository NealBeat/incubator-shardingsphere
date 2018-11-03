/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.parsing.antler.parser.factory;

import io.shardingsphere.core.constant.DatabaseType;
import io.shardingsphere.core.metadata.table.ShardingTableMetaData;
import io.shardingsphere.core.parsing.antler.VisitorRegistry;
import io.shardingsphere.core.parsing.antler.statement.visitor.StatementVisitor;
import io.shardingsphere.core.parsing.lexer.token.TokenType;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;
import io.shardingsphere.core.rule.ShardingRule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Use AST generate SQLStatement.
 *
 * @author duhongjun
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StatementFactory {

    /**
     * Parse SQL.
     *
     * @param dbType database type
     * @param tokenType token type
     * @param shardingRule sharding rule
     * @param sql input SQL text
     * @param shardingTableMetaData table meta data
     * @return SQL statement
     */
    public static SQLStatement parse(final DatabaseType dbType, final TokenType tokenType, final ShardingRule shardingRule, final String sql, final ShardingTableMetaData shardingTableMetaData) {
        ParserRuleContext rootNode = ParseTreeFactory.getParserTree(dbType, tokenType, shardingRule, sql);
        if (null != rootNode) {
            String commandName = getCommandName(rootNode);
            StatementVisitor visitor = VisitorRegistry.getInstance().getVisitor(dbType, commandName);
            if (null != visitor) {
                return visitor.visit(rootNode, shardingTableMetaData);
            }
        }
        return null;
    }

    /**
     * Get SQL command name from ast.
     *
     * @param node SQL statement root node
     * @return SQL command name
     */
    private static String getCommandName(final ParserRuleContext node) {
        String name = node.getClass().getSimpleName();
        int pos = name.indexOf("Context");
        if (pos > 0) {
            return name.substring(0, pos);
        }

        return name;
    }
}