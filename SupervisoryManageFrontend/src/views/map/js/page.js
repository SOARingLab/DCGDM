var threejsTitle = [
    'threejs视频教程',
    'threejs视频课程',
    'threejs视频',
    'threejs教程视频',
    'threejs课程视频'
]
var threejskcTitle = [
    'WebGL/Three.js前端高薪3D可视化',
    'WebGL/Three.js前端3D可视化教程',
    'Three.js前端3D可视化教程',
    'WebGL前端3D可视化教程',
    'WebGL 3D可视化教程',
    'Three.js前端3D可视化',
    'Three.js 3D可视化',
    '3D可视化Three.js视频教程',
    'Three.js视频教程3D可视化',
]
var feixiantu = ['飞线图', '迁徙图']
var labelzidingyi = ['label样式自定义', '自定义label样式', 'label自定义样式']
var pageList = {
    echarts: [
        {
            title: '定义颜色',
            id: 1,
            desc: 'echarts地图通过visualMap来事先设置好数据区间,根据区间来来显示地图的颜色,这里的visualMap数据是随机生成的,可以通过实例代码根据自己的需要配置颜色和数据区间.'
        }, {
            title: `${feixiantu[Math.floor(Math.random() * feixiantu.length)]}`,
            id: 2,
            desc: `echarts${feixiantu[Math.floor(Math.random() * feixiantu.length)]}实例,可以根据自己的需要配置飞线的颜色大小以及方向`
        }, {
            title: `${labelzidingyi[Math.floor(Math.random() * labelzidingyi.length)]}`,
            id: 3,
            desc: 'echarts通过scatter实例可以配置自己想要的图标,以及label文字的样式效果'
        }, {
            title: '局部颜色渐变',
            id: 4,
            desc: 'echarts地图渲染,通过echarts.graphic.LinearGradient线性渐变来实现地图区域颜色的渐变效果',

        }, {
            title: '全局颜色渐变',
            id: 5,
            desc: 'echarts地图渲染时,除了局部区域渐变外也会遇到整个地图的颜色渐变效果,我们可以通过areaColor的linear-gradient来实现我们想要的效果'
        }, {
            title: ' visualMap控制地图颜色',
            id: 6,
            desc: 'echarts地图使用visualMap通过一组颜色及一组数据来实现地图不同区域根据不同数据进行颜色变化',
        }, {
            title: '自定义引导线',
            id: 7,
            desc: 'echarts地图渲染中,会遇到多个地名凑到一块导致名称重叠的情况,这时候我们可以通过一条线指向对应的位置并通过label来进行标注的效果.'
        }, {
            title: '区域闪烁',
            id: 8,
            desc: 'echarts地图有时候为了突出某一块区域的重要性,会进行两个颜色的交替变化来告诉用户这一块需要重点关注,我们可以通过echarts的dispatchAction来实现我们想要的效果.'
        }, {
            title: '添加柱状图',
            id: 9,
            desc: 'echarts地图渲染时,为了让地图上的数据有一个对比的效果,会在地图上标注柱状图,以便于更直观的看出各个区域的数值.'
        }, {
            title: '水滴状气泡图',
            id: 10,
            desc: 'echarts地图渲染时,会通过一个水滴状小气泡来将数值渲染到地图上,我们可以可以通过scatter来实现我们想要的效果.'
        }, {
            title: '圆形波纹状气泡图',
            id: 11,
            desc: 'echarts地图渲染时,会通过effectScatter实现一个圆形外加波纹扩散效果的气泡来使页面看起来更酷炫.'
        }, {
            title: '点击地图插小旗',
            id: 12,
            desc: 'echarts地图渲染点击地图显示自定义效果实例,效果类似点击插小旗的效果,通过scatter实现.'
        }, {
            title: ' tooltip轮播',
            id: 13,
            desc: 'echarts地图实现类似tooltip轮播效果,通过effectScatter实现浮窗,并通过定时修改effectScatter的data来实现浮窗的移动.'
        }, {
            title: '阴影',
            id: 14,
            desc: 'echarts地图阴影效果实例,通过shadowColor控制阴影的颜色,通过shadowBlur控制范围,通过shadowOffsetX和shadowOffsetY控制偏移.'
        }, {
            title: '热力图',
            id: 15,
            desc: 'echarts通过heatmap实现热力图效果'
        }, {
            title: '指定区域高亮',
            id: 16,
            desc: 'echarts让指定区域变为选中高亮状态'
        }, {
            title: '点击跳转到指定页面',
            id: 17,
            desc: '为图标添加点击事件,点击图标,跳转到指定页面'
        }, {
            title: '3d地图',
            id: 18,
            desc: '使用的是echarts-gl结合echarts实现的3d地图效果实例'
        }, {
            title: '3d地图自定义图标',
            id: 19,
            desc: 'echarts 3d地图添加自定义图标实例'
        }, {
            title: '3d地图实例旋转动画',
            id: 20,
            desc: '使用的是echarts-gl结合echarts实现的3d地图效果实例'
        }, {
            title: '点击弹出自定义弹窗',
            id: 21,
            desc: '我们可以通过scatter来设置自定义样式,但是这种自定义局限性很大,如果要求的样式很复杂的话,scatter就不能很好的实现了,所以我们可以通过鼠标点击获取横纵坐标,然后通过css来定位到对应的位置.'
        }, {
            title: '点击地图获取经纬度',
            id: 22,
            desc: 'echarts地图渲染后,有时候会需要点击后获取到经纬度信息,通过经纬度信息来查询一些内容,这里我们可以通过convertFromPixel来获取经纬度.'
        }, {
            title: '根据经纬度显示自定义html弹窗',
            id: 23,
            desc: 'echarts地图我们可以通过像素坐标来将我们自定义的html弹窗定位到echarts地图指定位置,如果是固定的宽高的话,这没什么问题直接定位就可以了,如果是自适应宽高的地图我们可能没办法控制精度,我们可以使用经纬度来控制,当echarts地图渲染完成以后,我们可以通过convertToPixel将经纬度转为像素坐标,然后再渲染自定义弹窗,这样精度就比较准确了,而且也不会因为窗体宽高变化而导致弹窗出现偏移.'
        }],
    threejs: [
        {
            title: '3d地图',
            id: 1,
            desc: `通过threejs绘制的3d地图实例，如果想系统的学习threejs的话，可以观看${threejsTitle[Math.floor(Math.random() * threejsTitle.length)]} ${threejskcTitle[Math.floor(Math.random() * threejskcTitle.length)]}`
        }, {
            title: '3d地图label标签',
            id: 2,
            desc: `通过threejs绘制的3d地图标签实例，如果想系统的学习threejs的话，可以观看${threejsTitle[Math.floor(Math.random() * threejsTitle.length)]} ${threejskcTitle[Math.floor(Math.random() * threejskcTitle.length)]}`
        }, {
            title: '3d地图鼠标移入显示标签并高亮',
            id: 3,
            desc: `通过threejs绘制的3d地图鼠标移入显示标签并高亮，如果想系统的学习threejs的话，可以观看${threejsTitle[Math.floor(Math.random() * threejsTitle.length)]} ${threejskcTitle[Math.floor(Math.random() * threejskcTitle.length)]}`
        }],
}
