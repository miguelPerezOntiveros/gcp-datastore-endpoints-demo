package com.epamx.sharingIsCaring.endpoints;

import com.google.api.server.spi.auth.EspAuthenticator;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiIssuerAudience;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.UnauthorizedException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Logger;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

@Api(
  name = "list",
  version = "v1",
  clientIds = {"591358445297-t73o53vpohfbgomvj74laqo55snl9vn5.apps.googleusercontent.com"}
)

public class TaskList {
  private static final Logger log = Logger.getLogger(TaskList.class.getName());
  private DatastoreService datastoreService;
  private Datastore datastore;

  public TaskList(){
    datastoreService = DatastoreServiceFactory.getDatastoreService();
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
    Query query = new Query("Task").setFilter(new FilterPredicate("user", FilterOperator.EQUAL, user.getEmail()));
    List<com.google.appengine.api.datastore.Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
    log.info(new GsonBuilder().create().toJson(results));
    return new Result(new GsonBuilder().create().toJson(results));
  }
}
