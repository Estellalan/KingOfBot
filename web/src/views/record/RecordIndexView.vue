<template>
  <ContentField>对战列表</ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';

export default {
    components:{
        ContentField,
    },

    setup() {
        // $.ajax({
        //   url:"http://localhost:3000/user/account/token/",
        //   type:"post",
        //   data:{
        //     username:"ldh",
        //     password:"123456"
        //   },
        //   success(resp){
        //     console.log(resp);
        //   },
        //   error(resp){
        //     console.log(resp);
        //   },
        // });

        // $.ajax({
        //   url:"http://localhost:3000/user/account/info/",
        //   type:"get",
        //   headers:{
        //     Authorization:"Bearer "+"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkYTA4Y2U2NGM0MGE0ZmZkOTI5YmMzOTc2YzY5YzM0ZCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY5MTkxNTE4OSwiZXhwIjoxNjkzMTI0Nzg5fQ.dMZpgiy8aRDay4jYn6BQVF8LiFmacX489SbR1AY3mLQ"
        //   },
        //   success(resp){
        //     console.log(resp);
        //   },
        //   error(resp){
        //     console.log(resp);
        //   }
        // })

        // $.ajax({
        //   url:"http://localhost:3000/user/account/register/",
        //   type:"post",
        //   data:{
        //     username:"yxc1",
        //     password:"123",
        //     confirmedPassword:"123",
        //   },

        //   success(resp){
        //     console.log(resp);
        //   },
        //   error(resp){
        //     console.log(resp);
        //   }

        // })
    }
}
</script>

<style scoped>

</style>