<template>
    <div class="my-home">
        <div class="my-onlines">
            当前在线人数: <el-tag>{{onlines}}</el-tag>
        </div>
        <div id="main" style="width: 80%; height: 300px;"></div>
    </div>
</template>

<script>
    import axios from "axios"
    import { mapState } from "vuex"
    import moment from 'moment-timezone'

    export default {
        name: 'my-teacher',
        computed: {
            ...mapState('m_user', ['user'])
        },
        data() {
            return {
                onlines: 0,
                seriesX: [], // 天数
                seriesY: [] // 访问量
            };
        },
        async mounted() {
            await this.getOnlines()
            await this.get30DayStatistic()
            this.showEcharts();
        },
        methods: {
            async getOnlines() {
                const {data: res} = await axios.get('/teacher/online-students', {
                    params: {
                        id: this.user.id
                    }
                })
                if (res.success) {
                    this.onlines = res.content
                }
            },
            async get30DayStatistic() {
                const {data: res} = await axios.get('/teacher/snapshot')
                if (res.success) {
                    const tz = 'Asia/Shanghai'  //时区
                    //"2020-10-12 18:31:35"
                    res.content.forEach(item => {
                        // let date = moment.utc(item.date).tz(tz).format('YYYY-MM-DD HH:mm:ss')
                        let date = moment.utc(item.date).tz(tz).format('YYYY-MM-DD')
                        this.seriesX.push(date)
                        this.seriesY.push(item.views)
                    })
                }
            },
            showEcharts() {
                const chartDom = document.getElementById('main');
                const myChart = echarts.init(chartDom);
                const option = {
                    title: {
                        text: '30天趋势图'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['访问量']
                    },
                    grid: {
                        left: '1%',
                        right: '3%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: this.seriesX
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name: '访问量',
                            data: this.seriesY,
                            type: 'line'
                        }
                    ]
                };

                option && myChart.setOption(option);
            }
        }
    }
</script>

<style>
    .my-home{
        margin-top: 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .my-onlines {
        margin-bottom: 10px;
        font-size: 15px;
        font-family: "Helvetica Neue";
    }
</style>