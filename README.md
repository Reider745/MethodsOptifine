# Methods Optifine

Данный мод предназначен для оптимизации и повышения TPS(ticks per second). 
Он поддерживает как локальную, так и многопользовательскую игру, 
а также совместим с серверным ядром [ZoteCoreLoader](https://github.com/Reider745/ZoteCoreLoader/blob/NukkitMot/README-RU.md).

![MethodsOptifine](/mod_icon.png)

## Как работает мод?

[Рефлексия](https://javarush.com/groups/posts/513-reflection-api-refleksija-temnaja-storona-java) — это механизм исследования данных о программе во время её выполнения. Рефлексия позволяет получать информацию о полях, методах и конструкторах классов. Это основа работы Java-классов в [Rhino](https://github.com/mozilla/rhino) — JS-движке, который использует inner core.

Хотя рефлексия является мощным и универсальным инструментом, она достаточно медлительна.

Данный мод переписывает часть API inner core, которое используется повсеместно, устраняя рефлексию. Благодаря этому достигается ускорение работы при практически идеальной обратной совместимости.

# Сборка проекта

Установите [toolchain](https://github.com/zheka2304/innercore-mod-toolchain), поместите проект в папку toolchain.
Выполните установку classpath с помощью task "Reinstall components" 

Проект готов к сборке