/**
 * 数据验证工具类
 */
export class DataValidator {
  constructor() {
    this.templates = new Map() // 存储数据模板
  }

  /**
   * 添加数据模板
   * @param {string} project - 项目名称
   * @param {string} type - 数据类型(app/h5)
   * @param {Object} template - 模板数据
   */
  addTemplate(project, type, template) {
    const key = `${project}_${type}`
    this.templates.set(key, template)
  }

  /**
   * 获取项目的数据格式类型列表
   * @param {string} project - 项目名称
   * @returns {Array} 格式类型列表
   */
  getFormatTypes(project) {
    const types = []
    this.templates.forEach((template, key) => {
      if (key.startsWith(project)) {
        const type = key.split('_')[1]
        types.push(type)
      }
    })
    return [...new Set(types)]
  }

  /**
   * 验证数据格式
   * @param {Object} data - 要验证的数据
   * @param {string} project - 项目名称
   * @param {string} type - 数据类型
   * @returns {Object} 验证结果
   */
  validateFormat(data, project, type) {
    const template = this.templates.get(`${project}_${type}`)
    if (!template) {
      return {
        isValid: false,
        message: '未找到对应的数据模板',
        differences: {
          missing: [],
          extra: Object.keys(data)
        }
      }
    }

    const templateKeys = new Set(Object.keys(template))
    const dataKeys = new Set(Object.keys(data))

    // 找出缺失的字段
    const missing = Array.from(templateKeys).filter(key => !dataKeys.has(key))
    // 找出多余的字段
    const extra = Array.from(dataKeys).filter(key => !templateKeys.has(key))

    return {
      isValid: missing.length === 0 && extra.length === 0,
      message: missing.length === 0 && extra.length === 0 ? '数据格式校验通过' : '数据格式存在差异',
      differences: {
        missing,
        extra
      }
    }
  }
} 