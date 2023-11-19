const AC_GAME_OBJECTS = [];

export class AcGameObject{
     constructor(){//构造函数
        AC_GAME_OBJECTS.push(this);
        this.timedelta=0;//时间间隔，为了方便后面求速度
        this.has_called_start = false;//定义变量，表示某个函数是否被执行
     }

     start(){//只执行一次 

     }

     update() {//每一帧执行一次，除了第一帧之外
        
     }

     on_destrory(){//删除之前执行

     }

     destroy() {
        this.on_destrory();


        for(let i in AC_GAME_OBJECTS){//js中in遍历的是下表，of遍历的是值
            const obj = AC_GAME_OBJECTS[i];
            if(obj == this){
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
     }
}

let last_timestamp;//上一次执行的时刻

const step = timestamp => {//传入的timestamp参数为当前所执行的时刻
    for(let obj of AC_GAME_OBJECTS){
        if(!obj.has_called_start){
            obj.has_called_start=true;
            obj.start();//只执行第一帧
        }
        else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();//之后每一帧都执行update
        }
    }
    last_timestamp = timestamp;
    requestAnimationFrame(step);//写成递归，这样每一帧刷新都会调用step函数
}

requestAnimationFrame(step);//step是一个回调函数，该函数会在页面每次刷新前执行一遍 