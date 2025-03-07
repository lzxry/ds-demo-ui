<template>
  <div class="dashboard">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据统计</span>
          <el-select v-model="currentProject" placeholder="选择项目" style="width: 120px">
            <el-option label="拼多多" value="pdd" />
            <el-option label="京东" value="jd" />
            <el-option label="淘宝" value="tb" />
          </el-select>
        </div>
      </template>

      <!-- 数据概览卡片 -->
      <el-row :gutter="20" class="data-overview">
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">
                <span>今日下发量</span>
                <el-tooltip content="相比昨日" placement="top">
                  <span :class="['trend', todayStats.dispatchTrend >= 0 ? 'up' : 'down']">
                    {{ todayStats.dispatchTrend >= 0 ? '+' : '' }}{{ todayStats.dispatchTrend }}%
                  </span>
                </el-tooltip>
              </div>
            </template>
            <div class="stat-value">{{ todayStats.dispatchCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">
                <span>今日回传量</span>
                <el-tooltip content="相比昨日" placement="top">
                  <span :class="['trend', todayStats.returnTrend >= 0 ? 'up' : 'down']">
                    {{ todayStats.returnTrend >= 0 ? '+' : '' }}{{ todayStats.returnTrend }}%
                  </span>
                </el-tooltip>
              </div>
            </template>
            <div class="stat-value">{{ todayStats.returnCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">
                <span>本周下发量</span>
                <el-tooltip content="相比上周" placement="top">
                  <span :class="['trend', weekStats.dispatchTrend >= 0 ? 'up' : 'down']">
                    {{ weekStats.dispatchTrend >= 0 ? '+' : '' }}{{ weekStats.dispatchTrend }}%
                  </span>
                </el-tooltip>
              </div>
            </template>
            <div class="stat-value">{{ weekStats.dispatchCount }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">
                <span>本周回传量</span>
                <el-tooltip content="相比上周" placement="top">
                  <span :class="['trend', weekStats.returnTrend >= 0 ? 'up' : 'down']">
                    {{ weekStats.returnTrend >= 0 ? '+' : '' }}{{ weekStats.returnTrend }}%
                  </span>
                </el-tooltip>
              </div>
            </template>
            <div class="stat-value">{{ weekStats.returnCount }}</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 趋势图表 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="16">
          <el-card shadow="hover">
            <template #header>
              <div class="chart-header">
                <span>任务下发趋势</span>
                <el-radio-group v-model="dispatchTimeRange" size="small">
                  <el-radio-button label="week">周</el-radio-button>
                  <el-radio-button label="month">月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-container">
              <v-chart :option="dispatchChartOption" autoresize />
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <template #header>
              <div class="chart-header">
                <span>任务完成率</span>
              </div>
            </template>
            <div class="chart-container">
              <v-chart :option="completionRateChartOption" autoresize />
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <el-card shadow="hover">
            <template #header>
              <div class="chart-header">
                <span>Token任务分布</span>
              </div>
            </template>
            <div class="chart-container">
              <v-chart :option="tokenDistributionChartOption" autoresize />
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <template #header>
              <div class="chart-header">
                <span>任务类型分布</span>
              </div>
            </template>
            <div class="chart-container">
              <v-chart :option="taskTypeChartOption" autoresize />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart, BarChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
} from 'echarts/components'
import VChart from 'vue-echarts'

// 注册 ECharts 必须的组件
use([
  CanvasRenderer,
  LineChart,
  PieChart,
  BarChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
])

// 当前选中的项目
const currentProject = ref('pdd')

// 今日统计数据
const todayStats = ref({
  dispatchCount: 1234,
  returnCount: 1180,
  dispatchTrend: 12.5,
  returnTrend: 8.3
})

// 本周统计数据
const weekStats = ref({
  dispatchCount: 8567,
  returnCount: 8234,
  dispatchTrend: 5.2,
  returnTrend: 3.8
})

// 图表时间范围
const dispatchTimeRange = ref('week')

// 下发趋势图表配置
const dispatchChartOption = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['下发量', '回传量']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '下发量',
      type: 'line',
      smooth: true,
      data: [120, 132, 101, 134, 90, 230, 210]
    },
    {
      name: '回传量',
      type: 'line',
      smooth: true,
      data: [110, 125, 95, 130, 85, 220, 200]
    }
  ]
}))

// 完成率图表配置
const completionRateChartOption = computed(() => ({
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      radius: '50%',
      data: [
        { value: 1048, name: '已完成' },
        { value: 54, name: '未完成' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}))

// Token分布图表配置
const tokenDistributionChartOption = computed(() => ({
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '20',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: [
        { value: 1048, name: 'Token1' },
        { value: 735, name: 'Token2' },
        { value: 580, name: 'Token3' },
        { value: 484, name: 'Token4' },
        { value: 300, name: 'Token5' }
      ]
    }
  ]
}))

// 任务类型分布图表配置
const taskTypeChartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'value'
  },
  yAxis: {
    type: 'category',
    data: ['类型A', '类型B', '类型C', '类型D', '类型E']
  },
  series: [
    {
      name: '任务数量',
      type: 'bar',
      data: [320, 302, 301, 334, 390]
    }
  ]
}))
</script>

<style scoped lang="scss">
.dashboard {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .data-overview {
    margin-bottom: 20px;
  }

  .chart-row {
    margin-bottom: 20px;
  }

  .stat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    color: #606266;
  }

  .stat-value {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
    padding: 10px 0;
    color: #303133;
  }

  .trend {
    font-size: 12px;
    &.up {
      color: #67c23a;
    }
    &.down {
      color: #f56c6c;
    }
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .chart-container {
    height: 300px;
  }
}
</style> 