```java
public class TagResolver<T> {
    private static final Pattern textPattern = Pattern.compile("([+\\-,])([a-zA-Z0-9_]+)");
    private static final Pattern syntaxPattern = Pattern.compile("^(([+\\-,])([a-zA-Z0-9_]+))+$");
    private final Function<String, Predicate<T>> predicateBuilder;

    public static <T> TagResolver<T> of(Function<String, Predicate<T>> predicateBuilder) {
        return new TagResolver<>(predicateBuilder);
    }

    private TagResolver(Function<String, Predicate<T>> predicateBuilder) {
        this.predicateBuilder = predicateBuilder;
    }

    /**
     * 解析表达式成filter断言
     *
     * @param express 表达式字面量
     * @return
     */
    public Predicate<T> resolve(String express) {
        if (express == null) {
            return null;
        }
        String pureExpress = express.trim();
        if (pureExpress.isEmpty()) {
            throw new InvalidTagExpressionException("表达式不能为空白字符串");
        }
        if (!syntaxPattern.matcher(express.trim()).matches()) {
            throw new InvalidTagExpressionException("表达式解析失败");
        }
        return parseMatchResult(textPattern.matcher(express), predicateBuilder, it -> true);
    }

    private static <T> Predicate<T> doResolver(String expression, Function<String, Predicate<T>> predicateBuilder) {
        Predicate<T> root = it -> true;
        Matcher matcher = textPattern.matcher(expression);
        while (matcher.find()) {
            Assert.isTrue(matcher.groupCount() == 2, "系统故障!表达式结果数与预期不一致!");
            String operation = matcher.group(1);
            String tag = matcher.group(2);

            Predicate<T> predicate = predicateBuilder.apply(tag);
            root = append(root, predicate, operation);
        }
        return root;
    }

    /**
     * 从matcher得到表达式
     *
     * @param matcher          正则表达式的matcher
     * @param predicateBuilder 具体的业务判断逻辑生成函数
     * @param pre              上一个表达式结果断言
     * @return
     */
    private static <T> Predicate<T> parseMatchResult(Matcher matcher, Function<String, Predicate<T>> predicateBuilder, Predicate<T> pre) {
        if (!matcher.find()) {
            return pre;
        }
        Assert.isTrue(matcher.groupCount() == 2, "系统故障!表达式结果数与预期不一致!");
        String operation = matcher.group(1);
        String tag = matcher.group(2);

        Predicate<T> predicate = predicateBuilder.apply(tag);
        return parseMatchResult(matcher, predicateBuilder, append(pre, predicate, operation));
    }

    /**
     * 追加表达式
     *
     * @param pre       上一个表达式结果断言
     * @param current   当前的表达式结果断言
     * @param operation 当前的表达式操作符
     * @return
     */
    private static <T> Predicate<T> append(Predicate<T> pre, Predicate<T> current, String operation) {
        Assert.notNull(pre, "上一个结果断言不能为null");
        if (current == null) {
            return pre;
        }
        switch (operation) {
            case "+":
                return pre.and(current);
            case "-":
                return pre.and(current.negate());
            case ",":
                return pre.or(current);
            default:
                throw new RuntimeException("不支持的表达式操作符");
        }
    }
}

```


使用 

```java
public abstract class GroupParameter {
    protected static final Function<String, Predicate<GroupItemProperties>> predicateBuilder = tag ->
        (it) -> it.getTags() != null && it.getTags().contains(tag);
    protected static final TagResolver<GroupItemProperties> tagResolver = TagResolver.of(predicateBuilder);
}


AbstractDefaultFilter.doFilter(Collection<GroupItem> resources) {
    return resources.stream().filter(this.predicate).collect(Collectors.toList());
}

DefaultQueryFilter(TagResolver<GroupItem> tagResolver) {
    this.predicate = tagResolver.resolve(readOnly());
}
```



- 使用静态工厂方法构造类

```
Stream.of(1, 2, 3);
Integer.valueOf("3");

TagResolver.predicateBuilderOf()
```

- 递归函数 `append` 难以阅读


```java
public class TagFilter {
    private static final Pattern exprPattern = Pattern.compile("(?<op>[+\\-,])(?<tag>[a-zA-Z0-9_]+)");
    private static final Pattern syntaxPattern = Pattern.compile("^(([+\\-,])([a-zA-Z0-9_]+))+$");


    public static List<GroupItemProperties> filter(Collection<GroupItemProperties> items, String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            return (List<GroupItemProperties>) items;
        }

        Predicate<Collection<String>> predicate = parse(expr.trim());
        return items.stream()
                .filter(item -> predicate.test(item.getTags()))
                .collect(Collectors.toList());
    }

    private static Predicate<Collection<String>> parse(String expr) {
        if (!syntaxPattern.matcher(expr).matches()) {
            throw new InvalidTagExpressionException("tag表达式格式错误");
        }

        Predicate<Collection<String>> root = it -> true;
        Matcher matcher = exprPattern.matcher(expr);

        while (matcher.find()) {
            String op = matcher.group("op");
            String tag = matcher.group("tag");
            Predicate<Collection<String>> p = tags -> tags.contains(tag);

            switch (op) {
                case "+":
                    root = root.and(p);
                    break;
                case "-":
                    root = root.and(p.negate());
                    break;
                case ",":
                    root = root.or(p);
                    break;
            }
        }
        return root;
    }
}

```

使用
```java
// group  request 用户请求对象
List<GroupItemProperties> items = TagFilter.filter(group.getItems(), request.getTagExpr());
```