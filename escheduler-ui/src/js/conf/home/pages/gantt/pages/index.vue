<template>
<m-list-construction :title="$t('Datasource')">
  <div id="container">
    <div class="svgbox"></div>
    <div id="tag"></div>
  </div>
  </m-list-construction>
</template>
<script>
  import mListConstruction from '@/module/components/listConstruction/listConstruction'
export default {
  name: 'workflow',
  data() {
    return {};
  },
  props: {},
  methods: {},
  watch: {},
  created() {},
  mounted() {
     var taskArray = [
      {
        task: 'Jean Nicoli',
        // type: 'Margonajo',
        startTime: '2013-1-9 12:10', //year/month/day
        endTime: '2013-1-10 18:24',
        details: '120 vl, 30 er, 350 pax, md '
      },

      {
        task: 'Girolata',
        startTime: '2013-1-2 11:20',
        endTime: '2013-1-3 18:06',
        details: '48 ER'
      },

      {
        task: 'Surcouf',
        startTime: '2013-1-6 14:25',
        endTime: '2013-1-9 14:35'
      },

      {
        task: 'Queen Elisabeth',
        startTime: '2013-1-9 6:53',
        endTime: '2013-1-12 7:24',
        details: '2350 pax'
      },

      {
        task: 'Vega',
        startTime: '2013-1-12 00:24',
        endTime: '2013-1-14 14:04',
        details: 'Inaugurale'
      },
      {
        task: 'East Indian Ocean',
        startTime: '2013-1-8 1:45',
        endTime: '2013-1-13 3:00',
        details: 'L 320 m, l 58 m'
      },

      {
        task: 'crying',
        startTime: '2013-1-13 06:00',
        endTime: '2013-1-16 07:00'
      },

      {
        task: 'crying II',
        startTime: '2013-1-11 14:35',
        endTime: '2013-1-16 21:00'
      },
      {
        task: 'Belouga',
        startTime: '2013-1-7 8:12',
        endTime: '2013-1-14 9:30'
      },{
        task: 'East Indian Ocean',
        startTime: '2013-1-8 1:45',
        endTime: '2013-1-13 3:00',
        details: 'L 320 m, l 58 m'
      },

      {
        task: 'crying',
        startTime: '2013-1-13 06:00',
        endTime: '2013-1-16 07:00'
      },

      {
        task: 'crying II',
        startTime: '2013-1-11 14:35',
        endTime: '2013-1-16 21:00'
      },
      {
        task: 'Belouga',
        startTime: '2013-1-7 8:12',
        endTime: '2013-1-14 9:30'
      },{
        task: 'East Indian Ocean',
        startTime: '2013-1-8 1:45',
        endTime: '2013-1-13 3:00',
        details: 'L 320 m, l 58 m'
      },

      {
        task: 'crying',
        startTime: '2013-1-13 06:00',
        endTime: '2013-1-16 07:00'
      },

      {
        task: 'crying II',
        startTime: '2013-1-11 14:35',
        endTime: '2013-1-16 21:00'
      },
      {
        task: 'Belouga',
        startTime: '2013-1-7 8:12',
        endTime: '2013-1-14 9:30'
      },{
        task: 'East Indian Ocean',
        startTime: '2013-1-8 1:45',
        endTime: '2013-1-13 3:00',
        details: 'L 320 m, l 58 m'
      },

      {
        task: 'crying',
        startTime: '2013-1-13 06:00',
        endTime: '2013-1-16 07:00'
      },

      {
        task: 'crying II',
        startTime: '2013-1-11 14:35',
        endTime: '2013-1-16 21:00'
      },
      {
        task: 'Belouga',
        startTime: '2013-1-7 8:12',
        endTime: '2013-1-14 9:30'
      },{
        task: 'East Indian Ocean',
        startTime: '2013-1-8 1:45',
        endTime: '2013-1-13 3:00',
        details: 'L 320 m, l 58 m'
      },

      {
        task: 'crying',
        startTime: '2013-1-13 06:00',
        endTime: '2013-1-16 07:00'
      },

      {
        task: 'crying II',
        startTime: '2013-1-11 14:35',
        endTime: '2013-1-16 21:00'
      },
      {
        task: 'Belouga',
        startTime: '2013-1-7 8:12',
        endTime: '2013-1-14 9:30'
      },{
        task: 'East Indian Ocean',
        startTime: '2013-1-8 1:45',
        endTime: '2013-1-13 3:00',
        details: 'L 320 m, l 58 m'
      },

      {
        task: 'crying',
        startTime: '2013-1-13 06:00',
        endTime: '2013-1-16 07:00'
      },

      {
        task: 'crying II',
        startTime: '2013-1-11 14:35',
        endTime: '2013-1-16 21:00'
      },
      {
        task: 'Belouga',
        startTime: '2013-1-7 8:12',
        endTime: '2013-1-14 9:30'
      },
    ];
    var w = 1600;
    var h = 24*taskArray.length + 130;

    // 创建svg 画布
    var svg = d3
      .selectAll('.svgbox')
      .append('svg')
      .attr('width', w)
      .attr('height', h)
      .attr('class', 'svg');
    var dateFormat = d3.time.format('%Y-%m-%d %H:%M'); //%Y-%m-%d %H:%M:%S

    // 按照时间的顺序排列坐标轴
    var timeScale = d3.time
      .scale()
      .domain([
        d3.min(taskArray, function(d) {
          return dateFormat.parse(d.startTime);
        }),
        d3.max(taskArray, function(d) {
          return dateFormat.parse(d.endTime);
        })
      ])
      .range([0, w - 150]);

    var categories = new Array();

    for (var i = 0; i < taskArray.length; i++) {
      categories.push(taskArray[i].type);
    }

    var catsUnfiltered = categories; //for vert labels

    categories = checkUnique(categories);

    makeGant(taskArray, w, h);

    function makeGant(tasks, pageWidth, pageHeight) {
      var barHeight = 20;
      var gap = barHeight + 4;
      var topPadding = 75;
      var sidePadding = 0;   //整个图表距离左侧的padding

      var colorScale = d3.scale
        .linear()
        .domain([0, categories.length])
        .range(['#00B9FA', '#F95002'])
        .interpolate(d3.interpolateHcl);

      makeGrid(sidePadding, topPadding, pageWidth, pageHeight);
      drawRects(tasks, gap, topPadding, sidePadding, barHeight, colorScale, pageWidth, pageHeight);
      // vertLabels(gap, topPadding, sidePadding, barHeight, colorScale);
    }

    function drawRects(theArray, theGap, theTopPad, theSidePad, theBarHeight, theColorScale, w, h) {
      console.log(theSidePad)
      console.log(theArray, theGap, theTopPad, theSidePad, theBarHeight, theColorScale, w, h)
      // 横着的每一条暂时没用
      var bigRects = svg
        .append('g')
        .selectAll('rect')
        .data(theArray)
        .enter()
        .append('rect')
        .attr('x', 0)
        .attr('y', function(d, i) {
          return i * theGap + theTopPad - 2;
        })
        .attr('width', function(d) {
          return w - theSidePad / 2 ;  
        })
        .attr('height', theGap)
        .attr('stroke', 'none')
        .attr('fill', function(d) {
          for (var i = 0; i < categories.length; i++) {
            if (d.type == categories[i]) {
              return d3.rgb(theColorScale(i));
            }
          }
        })
        .attr('opacity', 0.2);

      
      // 每一条数据的盒子
      var rectangles = svg
        .append('g')
        .attr('class','data-box')
        .selectAll('rect')
        .data(theArray)
        .enter();

      // 定义可重复用的颜色组件
      var defs = svg.append( 'defs' );

      var colorfilter= defs.append( 'linearGradient' )
                                   .attr( 'id', 'gradientForegroundPurple' )
                                   
          colorfilter.append( 'stop' )
                        .attr( 'class', 'purpleForegroundStop1' )
                        .attr( 'stop-color', '#3960FF' )
                        .attr( 'offset', '0%' );

          colorfilter.append( 'stop' )
                        .attr( 'class', 'purpleForegroundStop2' )
                        .attr( 'stop-color', '#B08BFF' )
                        .attr( 'offset', '100%' );


      var innerRects = rectangles
        .append('rect')
        .attr('rx', 3)
        .attr('ry', 3)
        .attr('x', function(d) {
          return timeScale(dateFormat.parse(d.startTime)) + theSidePad;
        })
        .attr('y', function(d, i) {
          return i * theGap + theTopPad;
        })
        .attr('width', function(d) {
          return timeScale(dateFormat.parse(d.endTime)) - timeScale(dateFormat.parse(d.startTime));
        })
        .attr('height', theBarHeight)
        .attr('stroke', 'none')
        .attr('fill', 'url(#gradientForegroundPurple)')
        // .attr('fill', function(d) {
        //   for (var i = 0; i < categories.length; i++) {
        //     if (d.type == categories[i]) {
        //       return d3.rgb(theColorScale(i));
        //     }
        //   }
        // });

      var rectText = rectangles
        .append('text')
        .text(function(d) {
          return d.task;
        })
        .attr('x', function(d) {
          return (
            (timeScale(dateFormat.parse(d.endTime)) - timeScale(dateFormat.parse(d.startTime))) /
              2 +
            timeScale(dateFormat.parse(d.startTime)) +
            theSidePad
          );
        })
        .attr('y', function(d, i) {
          return i * theGap + 14 + theTopPad;
        })
        .attr('font-size', 11)
        .attr('text-anchor', 'middle')
        .attr('text-height', theBarHeight)
        .attr('fill', '#fff');
      // 每一条数据盒子里的文字上添加事件
      rectText
        .on('mouseover', function(e) {
          // console.log(this.x.animVal.getItem(this));
          var tag = '';

          if (d3.select(this).data()[0].details != undefined) {
            tag =
              'Navire: ' +
              d3.select(this).data()[0].task +
              '<br/>' +
              'Quai: ' +
              d3.select(this).data()[0].type +
              '<br/>' +
              'Accostage: ' +
              d3.select(this).data()[0].startTime +
              '<br/>' +
              'Appareillage: ' +
              d3.select(this).data()[0].endTime +
              '<br/>' +
              'Observations: ' +
              d3.select(this).data()[0].details;
          } else {
            tag =
              'Navire: ' +
              d3.select(this).data()[0].task +
              '<br/>' +
              'Quai: ' +
              d3.select(this).data()[0].type +
              '<br/>' +
              'Accostage: ' +
              d3.select(this).data()[0].startTime +
              '<br/>' +
              'Appareillage: ' +
              d3.select(this).data()[0].endTime;
          }
          var output = document.getElementById('tag');

          var x = this.x.animVal.getItem(this) + 'px';
          var y = this.y.animVal.getItem(this) + 25 + 'px';

          output.innerHTML = tag;
          output.style.top = y;
          output.style.left = x;
          output.style.display = 'block';
        })
        .on('mouseout', function() {
          var output = document.getElementById('tag');
          output.style.display = 'none';
        });
      // 每一条数据盒子上添加事件
      innerRects
        .on('click', function(e) {
            console.log('click',e)
          })
        .on('mouseover', function(e) {
          //console.log(this);
          var tag = '';

          if (d3.select(this).data()[0].details != undefined) {
            tag =
              'Navire: ' +
              d3.select(this).data()[0].task +
              '<br/>' +
              'Quai: ' +
              d3.select(this).data()[0].type +
              '<br/>' +
              'Accostage: ' +
              d3.select(this).data()[0].startTime +
              '<br/>' +
              'Appareillage: ' +
              d3.select(this).data()[0].endTime +
              '<br/>' +
              'Observations: ' +
              d3.select(this).data()[0].details;
          } else {
            tag =
              'Navire: ' +
              d3.select(this).data()[0].task +
              '<br/>' +
              'Quai: ' +
              d3.select(this).data()[0].type +
              '<br/>' +
              'Appareillage: ' +
              d3.select(this).data()[0].startTime +
              '<br/>' +
              'Observations: ' +
              d3.select(this).data()[0].endTime;
          }
          var output = document.getElementById('tag');

          var x = this.x.animVal.value + this.width.animVal.value / 2 + 'px';
          var y = this.y.animVal.value + 25 + 'px';

          output.innerHTML = tag;
          output.style.top = y;
          output.style.left = x;
          output.style.display = 'block';
        })
        .on('mouseout', function() {
          var output = document.getElementById('tag');
          output.style.display = 'none';
        });
    }

    // 坐标轴
    function makeGrid(theSidePad, theTopPad, w, h) {
      var xAxis = d3.svg
        .axis()
        .scale(timeScale)   //设置刻度尺，并返回轴
        .orient('bottom')
        .ticks(d3.time.days, 1)
        // .tickSize(-h + theTopPad + 20, 0, 0)    // 设置线的高度
        .tickSize(-h + theTopPad + 50, 0, 0)    // 设置线的高度
        .tickFormat(d3.time.format('%d %b'));
      var grid = svg
        .append('g')
        .attr('class', 'grid')
        .attr('transform', 'translate(' + theSidePad + ', ' + (h - 50) + ')')
        .call(xAxis)
        .selectAll('text')
        .attr('class', 'axis-text')
        .style('text-anchor', 'middle')
        .attr('fill', '#000')
        .attr('stroke', 'none')
        .attr('font-size', 14)
        .attr('dy', '2em');       // 距离线的距离
    }

    // 竖着的左侧的展示 （已注释）
    function vertLabels(theGap, theTopPad, theSidePad, theBarHeight, theColorScale) {
      var numOccurances = new Array();
      var prevGap = 0;

      for (var i = 0; i < categories.length; i++) {
        numOccurances[i] = [categories[i], getCount(categories[i], catsUnfiltered)];
      }

      var axisText = svg
        .append('g') //without doing this, impossible to put grid lines behind text
        .attr('class', 'g-text')
        .selectAll('text')
        .data(numOccurances)
        .enter()
        .append('text')
        .text(function(d) {
          return d[0];
        })
        .attr('x', 10)
        .attr('y', function(d, i) {
          if (i > 0) {
            for (var j = 0; j < i; j++) {
              prevGap += numOccurances[i - 1][1];
              // console.log(prevGap);
              return d[1] * theGap / 2 + prevGap * theGap + theTopPad;
            }
          } else {
            return d[1] * theGap / 2 + theTopPad;
          }
        })
        .attr('font-size', 11)
        .attr('text-anchor', 'start')
        .attr('text-height', 14)
        .attr('fill', function(d) {
          for (var i = 0; i < categories.length; i++) {
            if (d[0] == categories[i]) {
              //  console.log("true!");
              return d3.rgb(theColorScale(i)).darker();
            }
          }
        });
    }

    //from this stackexchange question: http://stackoverflow.com/questions/1890203/unique-for-arrays-in-javascript
    function checkUnique(arr) {
      var hash = {},
        result = [];
      for (var i = 0, l = arr.length; i < l; ++i) {
        if (!hash.hasOwnProperty(arr[i])) {
          //it works with objects! in FF, at least
          hash[arr[i]] = true;
          result.push(arr[i]);
        }
      }
      return result;
    }

    //from this stackexchange question: http://stackoverflow.com/questions/14227981/count-how-many-strings-in-an-array-have-duplicates-in-the-same-array
    function getCounts(arr) {
      var i = arr.length, // var to loop over
        obj = {}; // obj to store results
      while (i) obj[arr[--i]] = (obj[arr[i]] || 0) + 1; // count occurrences
      return obj;
    }

    // get specific from everything
    function getCount(word, arr) {
      return getCounts(arr)[word] || 0;
    }
  }
};
</script>
<style>
#container {
  margin: 0 auto;
  position: relative;
  width: 1600px;
  overflow: visible;
}

.svgbox {
  /* position: relative;
	float:left;
	overflow-y: scroll;
	height: 100px; */
  width: 1600px;
  height: 100vh;
  overflow: visible;
  position: absolute;
}

.grid .tick {
  stroke: lightgrey;
  opacity: 0.3;
  shape-rendering: crispEdges;
}
.grid path {
  stroke-width: 0;
}

#tag {
  color: white;
  background: #fa283d;
  width: 150px;
  position: absolute;
  display: none;
  padding: 3px 6px;
  margin-left: -80px;
  font-size: 11px;
}

#tag:before {
  border: solid transparent;
  content: ' ';
  height: 0;
  left: 50%;
  margin-left: -5px;
  position: absolute;
  width: 0;
  border-width: 10px;
  border-bottom-color: #fa283d;
  top: -20px;
}
</style>

