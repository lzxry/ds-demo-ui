<template>
  <div class="data-statistics">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据统计</span>
        </div>
      </template>

      <!-- 统计卡片 -->
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">今日任务数</div>
            </template>
            <div class="stat-value">150</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">今日完成数</div>
            </template>
            <div class="stat-value">120</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">今日金额</div>
            </template>
            <div class="stat-value">¥1,500</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <template #header>
              <div class="stat-header">累计任务数</div>
            </template>
            <div class="stat-value">1,500</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">任务完成趋势</div>
            </template>
            <div class="chart-container">
              <!-- 这里可以添加图表组件 -->
              <div class="chart-placeholder">图表区域</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">金额统计</div>
            </template>
            <div class="chart-container">
              <!-- 这里可以添加图表组件 -->
              <div class="chart-placeholder">图表区域</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
// 这里可以添加数据统计相关的逻辑
</script>

<style scoped lang="scss">
.data-statistics {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .stat-header {
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

  .chart-container {
    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .chart-placeholder {
    color: #909399;
    font-size: 14px;
  }
}
</style> 