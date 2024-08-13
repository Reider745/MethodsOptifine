type TestFunc = (x: number, y: number, z: number, id: number) => void;

//let getRegion: (id: number) => BlockSource = ModAPI.requireGlobal("BlockSource.getOriginal");
//let Classic: TestFunc = ModAPI.requireGlobal("Classic");
//let NotReflect: TestFunc = ModAPI.requireGlobal("NotReflect");

function getTime(): number {
    return java.lang.System.currentTimeMillis();
}

function testPerfomance(prefix: string, func: () => void): void {
    let start = getTime();
    func();
    Game.message(prefix+"/ Test time: "+(getTime() - start));
}

/*Callback.addCallback("ItemUse", (coords, item, block, is, player) => {
    testPerfomance("Classic", () => {
        let region = getRegion(Entity.getDimension(player));
        for(let x = 0;x < 50;x++)
            for(let y = 0;y < 50;y++)
                for(let z = 0;z < 50;z++)
                    region.setBlock(coords.x + x, coords.y + y, coords.z + z, 1, 0);
    });

    testPerfomance("NotReflect", () => {
        let region = BlockSource.getDefaultForActor(player);
        for(let x = 0;x < 50;x++)
            for(let y = 0;y < 50;y++)
                for(let z = 0;z < 50;z++)
                    region.setBlock(coords.x + x, coords.y + y, coords.z + z, 5, 0);
    });
});*/