package jkit.entry;

public interface Deps {

    interface Param {
        CommandParam<?> name = CommandParam.of("name", String.class);
        CommandParam<?> flag = CommandParam.of("flag", CommandFlag.class);
        CommandParam<?> num = CommandParam.of("num", Integer.class);
        CommandParam<?> arr = CommandParam.of("likes", Integer.class, true);
    }

    interface Def {
        CommandDef test = CommandDef.builder()
            .name("test")
            .param(Param.name)
            .build();
    }

}
