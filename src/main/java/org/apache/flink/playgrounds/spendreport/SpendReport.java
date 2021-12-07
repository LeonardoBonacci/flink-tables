/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.playgrounds.spendreport;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

public class SpendReport {

    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance().build();
        TableEnvironment tEnv = TableEnvironment.create(settings);

	      tEnv.executeSql("CREATE TABLE Orders (\n" +
	      "    price        DECIMAL(32,2),\n" +
	      "    buyer        ROW<first_name STRING, last_name STRING>,\n" +
	      "    order_time   TIMESTAMP(3)\n" +
          ") WITH (\n" +
          "	  'connector' = 'datagen',\n" +
          "	  'rows-per-second' = '2',\n" +
          "	  'number-of-rows' = '10'\n" + 
	      ")");

	      tEnv.executeSql("CREATE TABLE printOrders WITH ('connector' = 'print')\n" +
	      "LIKE Orders (EXCLUDING ALL)");

	      Table orders = tEnv.from("Orders");
	      orders.executeInsert("printOrders");
    }
}
