import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx,parent){//这里传的参数是画布和画布父元素
        super();//执行基类构造函数

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;//定义一个格子的绝对距离，我们计划的地图为13*13

        this.rows=13;
        this.cols=14;

        this.inner_walls_count=20;
        this.walls = [];

        this.snakes = [
            new Snake({id:0,color:"#4876EC",r:this.rows-2,c:1},this),
            new Snake({id:1,color:"#F94848",r:1,c:this.cols-2},this),
        ];

    }

    check_connectivity(g,sx,sy,tx,ty){
         if(sx==tx&&sy==ty) return true;
         g[sx][sy]=true;

         let dx = [-1,0,1,0];
         let dy = [0,1,0,-1];

         for(let i=0;i<4;i++){
            let x=sx+dx[i];
            let y=sy+dy[i];

            if(!g[x][y]&&this.check_connectivity(g,x,y,tx,ty))return true;
         }

         return false;
    }

    create_walls(){
        const g = [];
        for(let r=0;r<=this.rows;r++){
            g[r] = [];
            for(let c=0;c<=this.cols;c++){
                g[r][c]=false;
            }
        }

        //给四周加上障碍物
        for(let r=0;r<=this.rows;r++){
            g[r][0]=g[r][this.cols-1]=true;
        }

        for(let c=0;c<=this.cols;c++){
            g[0][c]=g[this.rows-1][c]=true;
        }

        //创建随机障碍物
        for(let i=0;i<this.inner_walls_count/2;i++){
            for(let j=0;j<1000;j++){
                let r = parseInt(Math.random()*this.rows);
                let c = parseInt(Math.random()*this.cols);

                let x=this.rows-1-r;
                let y=this.cols-1-c;

                if(g[r][c]||g[x][y])continue;

                if(r==this.rows-2&&c==1)continue;
                if(r==1&&c==this.cols-2)continue;

                g[r][c]=g[x][y]=true;
                break;
            }
        }

        const copy_g = JSON.parse(JSON.stringify(g));

        if(!this.check_connectivity(copy_g,this.rows-2,1,1,this.cols-2)) return false;

        for(let r=0;r<=this.rows;r++){
            for(let c=0;c<=this.cols;c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
        }

        return true;
    }

    add_listening_events() {
        this.ctx.canvas.focus();

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === 'w') snake0.set_direction(0);
            else if (e.key === 'd') snake0.set_direction(1);
            else if (e.key === 's') snake0.set_direction(2);
            else if (e.key === 'a') snake0.set_direction(3);
            else if (e.key === 'ArrowUp') snake1.set_direction(0);
            else if (e.key === 'ArrowRight') snake1.set_direction(1);
            else if (e.key === 'ArrowDown') snake1.set_direction(2);
            else if (e.key === 'ArrowLeft') snake1.set_direction(3);
        //     console.log(e.key);
        //     console.log("0"+snake0.status+" "+snake0.direction);
        //    console.log("1"+snake1.status+" "+snake1.direction);
        });
    }

    start() {
        for (let i = 0; i < 1000; i ++ ) 
            if (this.create_walls())
                break;
        
        this.add_listening_events();
    }


    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows));
        this.ctx.canvas.width=this.L*this.cols;
        this.ctx.canvas.height=this.L*this.rows;
    }

    check_ready(){//判断两条蛇是否都准备好下一回合了
        //console.log("check");
        const [snake0,snake1]=this.snakes;

        //console.log("0"+snake0.status+" "+snake0.direction);
        //console.log("1"+snake1.status+" "+snake1.direction);

        for(const snake of this.snakes){
         if(snake.status !== "idle") return false;
         if(snake.direction === -1) return false;//js中判断相等，要三个等号
        }
        // console.log("ready");
        return true;
     }

     next_step(){//让两条蛇进入下一回合
        for(const snake of this.snakes){
            snake.next_step();
        }
     }

     check_valid(cell){//检测目标位置是否合法，蛇身和另一条的蛇以及障碍物不能相撞
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c) return false;
        }

        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){//当蛇尾会前进的时候，蛇尾不要判断
                k--;
            }

            for(let i = 0;i < k;i ++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                return false;
            }
        }

        return true;

    }


    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
        this.render();//每一帧执行一次渲染函数
    }

    render(){//渲染
        const color_even="#AAD751",color_odd="#A2D048";

        for( let r=0;r<this.rows;r++){
            for(let c=0;c<this.cols;c++){
                if((r+c)%2==0){
                    this.ctx.fillStyle = color_even;
                }
                else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c*this.L,r*this.L,this.L,this.L);
            }
        }
    }
}