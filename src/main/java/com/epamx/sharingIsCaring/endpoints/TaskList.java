package com.epamx.sharingIsCaring.endpoints;

import com.google.api.server.spi.auth.EspAuthenticator;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.UnauthorizedException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Logger;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.collect.Lists;

// https://googleapis.github.io/google-cloud-java/google-cloud-clients/apidocs/index.html?com/google/cloud/datastore/package-summary.html

@Api(
  name = "list",
  version = "v1",
  clientIds = {"591358445297-t73o53vpohfbgomvj74laqo55snl9vn5.apps.googleusercontent.com"}
)

public class TaskList {
  private static final Logger log = Logger.getLogger(TaskList.class.getName());
  private Datastore datastore;

  public TaskList(){
    datastore = DatastoreOptions.getDefaultInstance().getService();
  }

  @ApiMethod(path="task", httpMethod = ApiMethod.HttpMethod.POST)
  public Result createTask(User user, Task task) {
    IncompleteKey key = datastore.newKeyFactory().setKind("Task").newKey();
    FullEntity<IncompleteKey> entity = Entity.newBuilder(key)
    .set("body", task.body)
    .set("title", task.title)
    .set("user", user.getEmail())
    .build();
    datastore.add(entity);
    log.info("Posted a task.");
    return new Result("Posted successfully");
  }

  @ApiMethod(path="task")
  public Result getTasks(User user) {
    StructuredQuery<Entity> query = Query.newEntityQueryBuilder()
      .setKind("Task")
      .setFilter(PropertyFilter.eq("user", user.getEmail()))
      .build();
    QueryResults<Entity> results = datastore.run(query);
    List<Entity> entities = Lists.newArrayList();
    while (results.hasNext()) {
      Entity result = results.next();
      entities.add(result);
    }
    log.info(new GsonBuilder().create().toJson(results));
    return new Result(new GsonBuilder().create().toJson(results));
  }
}
