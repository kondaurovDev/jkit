package jkit.entry;

public interface Deps {

    interface Prop {
        PropDef<?> name = PropDef.of("name", String.class);
        PropDef<?> flag = PropDef.of("flag", CommandFlag.class);
        PropDef<?> num = PropDef.of("num", Integer.class);
        PropDef<?> arr = PropDef.of("likes", Integer.class, true);
    }

    interface Def {
        CommandDef test = CommandDef.builder()
            .name("test")
            .param(Prop.name)
            .required(Prop.name.getName())
            .build();
    }

}
