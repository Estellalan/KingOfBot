<template>
    <content-field v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3 row">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="test" class="form-control" id="username" placeholder="请输入用户名">
                    </div>

                    <div class="mb-3 row">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>

                    <div class="error-message">{{ error_message }}</div>

                    <button type="submit" class="btn btn-primary">登陆</button>
                </form>
            </div>
        </div>
  </content-field>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '@/router/index';


export default {
    components:{
        ContentField,
    },
    setup(){
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        const jwt_token = localStorage.getItem("jwt_token");
        if(jwt_token){
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success(){
                    router.push({name:"home"});
                    store.commit("updatePullingInfo", false);
                },
                error(){
                    store.commit("updatePullingInfo", false);
                }
            })
        }
        else{
            store.commit("updatePullingInfo", false);
        }

        const login = () =>{
            //如果想调用store中action中的函数，就要使用dispatch

           // console.log(username.value+" "+password.value);

            store.dispatch("login",{
                username:username.value,
                password:password.value,
                success(resp){
                    store.dispatch("getinfo",{
                        success(resp){
                            router.push({name:'home'});
                            //console.log(store.state.user);
                        }
                    })
                    
                },
                error(resp){
                    error_message.value="用户名或密码错误";
                    
                }
            })
        }

        return{
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>
button {
    width:100%;
}

div.error-message{
    color: red;
}

</style>