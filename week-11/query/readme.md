
## before
Controller
```java
@RestController
@RequestMapping("/api/v1/resgroups")
public class ResourceGroupController extends AbstractApiController {
    private final IGroupCrudService groupCrudService;
    private final IGroupBizService groupBizService;

    @RequestMapping(path = "/{id}/query", method = {RequestMethod.POST}, name = "查询")
    public Collection<QueryResult> query(@PathVariable Integer id, @RequestParam String database, @RequestBody GroupQueryStatement statement) {
        return groupBizService.query(id, database, statement);
    }
}
```

QueryService
```java
@Service
public class GroupBizServiceImpl implements IGroupBizService {
    private final GroupRepository groupRepository;
    private final ResourceGroupManager groupManager;

     @Override
    public Collection<QueryResult> query(Integer gid, String database, GroupQueryStatement statement) {
        List<SQLStatement> statements = SQLUtils.parseStatements(statement.getStmt(), JdbcConstants.MYSQL);
        if (!statements.stream().allMatch(SqlValidator::legalQuery)) {
            throw new RuntimeException("非法的查询语句");
        }
        String[] scripts = statements.stream().map(Objects::toString).toArray(String[]::new);
        AbstractTaskExecutorBuilder executorBuilder = new DatabaseSingleQueryExecutorBuilder(database, scripts);
        GroupParameter groupTask = new DatabaseGroupParameter(gid, statement.getTag(), executorBuilder);

        Collection<QueryResult> results = groupManager.submitTask(groupTask);
        if (results == null || results.size() != 1) {
            return null;
        }
        return results;
    }
}
```


## after

Controller
```java
@RestController
@RequestMapping("/api/v1/resgroups")
public class ResourceGroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private QueryService queryService;
    
    /**
     * @param gid      资源组id
     * @param database 资源数据库名称或id
     * @param request  用户请求 body 对象
     * @return QueryResult 查询结果
     */
    @RequestMapping(path = "/resgroup/{gid}/query", method = {RequestMethod.POST}, name = "查询")
    public QueryResult query(@PathVariable Integer gid, @RequestParam String database, @RequestBody QueryRequest request) {
        Group group = groupService.get(gid);
        if (group == null) {
            return new QueryResult().setErrorMsg("资源组不存在");
        } else if (group.getItems().size() == 0) {
            return new QueryResult().setErrorMsg("资源组内没有资源");
        }
        request.setDatabase(database);
        return queryService.query(group, request);
    }

```


QueryService
```java
@Service
public class QueryService {
    
    @Autowired
    privete StatementService statementService;
    
    private ExecutorService executor = Executors.newFixedThreadPool(50);

    /**
     * 根据用户请求进行查询
     */
    public QueryResult query(Group group, QueryRequest request) {
        if(!statementService.validate(request.getStatements(), group.getType())) {
            return new QueryResult().setErrorMsg("Statements 语法错误");
        }

        List<Group> items = TagFilter.filter(group.getItems(), request.getTagExpr());

        switch (group.getType()) {
            case MySQL:
                TaskFactory<GroupItem, ResourceResult> factory = new DatabaseTaskFactory(
                        TaskParameter.toDatabaseQuery(request.getDatabase(), request.getStatements()));
            case Redis:
                // TODO
        }

        Strategy<GroupItem, ResourceResult> strategy = Strategy.create(StrategyType.valueOf(request.getStrategy()), executor);
        
        try {
            List<ResourceResult> results = Strategy.create(StrategyType.valueOf(strategy), executor).queryOn(items, factory);
            return new QueryResult().setData(results);
        } catch (InterruptedException e) {
            return new QueryResult().setErrorMsg(e.getCause().getMessage());
        }
    }
}
```