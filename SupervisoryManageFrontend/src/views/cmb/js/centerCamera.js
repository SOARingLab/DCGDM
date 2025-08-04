/*centerCamera：模型居中,同时调整正投影相机渲染范围*/
import * as THREE from './threejs/build/three.module.js';

function centerCamera(mapGroup, camera, k) {
    // 地图mapGroup的包围盒计算
    var box3 = new THREE.Box3(); //创建一个包围盒
    box3.expandByObject(mapGroup); // .expandByObject()方法：计算层级模型group包围盒
    var center = new THREE.Vector3(); //scaleV3表示包围盒的几何体中心
    box3.getCenter(center); // .getCenter()计算一个层级模型对应包围盒的几何体中心
    // console.log('查看几何中心', center);
    // 重新设置模型的位置
    mapGroup.position.x = mapGroup.position.x - center.x;
    mapGroup.position.y = mapGroup.position.y - center.y;
    mapGroup.position.z = mapGroup.position.z - center.z;


    /*可以根据中国地图mapGroup的包围盒尺寸设置相机参数s */
    var scaleV3 = new THREE.Vector3(); //scaleV3表示包围盒长宽高尺寸
    box3.getSize(scaleV3) // .getSize()计算包围盒长宽高尺寸
    console.log('查看包围盒尺寸', scaleV3)
    // frame.js文件中var s = 150; 150更改为scaleV3.x/2
    var maxL = maxLFun(scaleV3);
    //重新设置s值 乘以0.5适当缩小显示范围，地图占canvas画布比例更大，自然渲染范围更大
    var s = maxL / 2 * 0.5;
    camera.left = -s * k;
    camera.right = s * k;
    camera.top = s;
    camera.bottom = -s;
    //更新相机视图矩阵
    camera.updateProjectionMatrix();
    return maxL
}

// 获取地图的中心点
function getBoxCenter(mapGroup) {
    // 地图mapGroup的包围盒计算
    var box3 = new THREE.Box3(); //创建一个包围盒
    box3.expandByObject(mapGroup); // .expandByObject()方法：计算层级模型group包围盒
    var center = new THREE.Vector3(); //scaleV3表示包围盒的几何体中心
    box3.getCenter(center); // .getCenter()计算一个层级模型对应包围盒的几何体中心
    return center
}

// 获取地图的大小
function getBoxSize(mapGroup) {
    // 地图mapGroup的包围盒计算
    var box3 = new THREE.Box3(); //创建一个包围盒
    box3.expandByObject(mapGroup); // .expandByObject()方法：计算层级模型group包围盒
    var scaleV3 = new THREE.Vector3(); //scaleV3表示包围盒长宽高尺寸
    box3.getSize(scaleV3) // .getSize()计算包围盒长宽高尺寸
    return scaleV3
}

// 计算包围盒的最大边长
function maxLFun(v3) {
    var max;
    if (v3.x > v3.y) {
        max = v3.x
    } else {
        max = v3.y
    }
    if (max > v3.z) {
    } else {
        max = v3.z
    }
    return max;
}

export {centerCamera, getBoxCenter, getBoxSize};
