<template>
  <ContentField>我的Bot</ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import store from '@/store';

export default {
    components:{
        ContentField,
    },
    setup(){
      $.ajax({
        url:"http://localhost:3000/user/bot/add/",
        type:"post",
        data:{
          title:"Bot的标题",
          description:"Bot的描述",
          content:"Bot的代码",
        },
        headers:{
          Authorization:"Bearer "+store.state.user.token,
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
      })
    }
}
</script>

<style scoped>

</style>