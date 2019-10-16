/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.pipeline.sql.plugins.sources;

import com.google.common.base.Strings;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.pipeline.sql.api.core.Column;
import io.cdap.pipeline.sql.api.core.StructuredQuery;
import io.cdap.pipeline.sql.api.core.Table;
import io.cdap.pipeline.sql.api.template.SQLSource;

/**
 * A BigQuery SQL source.
 */
@Plugin(type = SQLSource.PLUGIN_TYPE)
@Name("BigQueryTable")
@Description("A BigQuery source.")
public class BigQuerySQLSource extends SQLSource {
  private final BigQuerySQLSourceConfig config;

  public BigQuerySQLSource(BigQuerySQLSourceConfig config) {
    this.config = config;
  }

  @Override
  public StructuredQuery constructQuery() {
    if (Strings.isNullOrEmpty(config.getProject())) {
      throw new IllegalArgumentException("Destination project string must be provided.");
    }
    if (Strings.isNullOrEmpty(config.getDataset())) {
      throw new IllegalArgumentException("Destination dataset string must be provided.");
    }
    if (Strings.isNullOrEmpty(config.getTable())) {
      throw new IllegalArgumentException("Destination table string must be provided.");
    }
    Table table = new Table(config.getProject(), config.getDataset(), config.getTable(), null);
    Column allColumns = Column.builder("*").setFrom(table).build();
    return StructuredQuery.builder().select(allColumns).from(table).build();
  }

  /**
   * The configuration class for a BigQuery SQL source.
   */
  public static class BigQuerySQLSourceConfig extends PluginConfig {
    public static final String PROJECT_NAME = "project";
    public static final String DATASET_NAME = "dataset";
    public static final String TABLE_NAME = "table";

    @Name(PROJECT_NAME)
    @Description("The destination project.")
    private String project;

    @Name(DATASET_NAME)
    @Description("The destination dataset.")
    private String dataset;

    @Name(TABLE_NAME)
    @Description("The destination table.")
    private String table;

    public BigQuerySQLSourceConfig(String project, String dataset, String table) {
      this.project = project;
      this.dataset = dataset;
      this.table = table;
    }

    public String getProject() {
      return project;
    }

    public String getDataset() {
      return dataset;
    }

    public String getTable() {
      return table;
    }
  }
}