$(function () {
	$('.slideshow').each(function(){
		// 계층적으로 선택하는게 좋다
		var $container = $(this);
		var $slideGroup = $container.find('.slideshow-slides');
		var $slides = $slideGroup.find('.slide');
		var $nav = $container.find('.slideshow-nav');
		var $indicator = $container.find('.slideshow-indicator');
		
		var slideCount = $slides.length;
		var indicatorHTML = '';
		var currentIndex = 0;
		var aniTime = 500;
		var timerTime = 5000;
		var easing = 'easeInOutSine';
		var timer = '';
		
		$slides.each(function(indexNum){
			$(this).css({left : 100*indexNum + '%'});
			indicatorHTML = indicatorHTML + '<a href="#">' + (indexNum+1) + '</a>';
		});
		$indicator.html(indicatorHTML);
		
		function goToSlide(index){ // 이미지 바꾸기
			$slideGroup.animate({left : 100*index*(-1) + "%"}, aniTime, easing);
			currentIndex = index;
			updateNav();
		}
		
		function updateNav(){ // indicator 흰색에서 검정색으로, 화살표도 바꾸기
			var $navPrev = $nav.find('.prev');
			var $navNext = $nav.find('.next');
			if(currentIndex === 0){ //첫번째 이미지일때
				$navPrev.addClass('dispNone');
			} else {
				$navPrev.removeClass('dispNone');
			}
			if(currentIndex === (slideCount-1)){ //첫번째 이미지일때
				$navNext.addClass('dispNone');
			} else {
				$navNext.removeClass('dispNone');
			}
			$indicator.find('a').removeClass('active').eq(currentIndex).addClass('active');
		}
		
		function startTimer(){ // 마우스가 빠지면 재가동
			timer = setInterval(function(){
				var nextIndex = (currentIndex + 1) % slideCount;
				goToSlide(nextIndex)
			}, timerTime);
		}
		
		function stopTimer(){ // 마우스가 올라가면 스톱
			clearInterval(timer);
		}
		
		// 이벤트
		$nav.on('click', 'a', function(e){ // nav에서 a태그를 click 했을 때 어떤 작업(콜백함수)
			
			e.preventDefault(); // 앵커태그에서 이뤄져야하는 작업을 취소한다. 아래와 같은 의미
			// return false; // 앵커태그에 이벤트가 걸렸을 때 고민해봐야 한다. (false : 페이지 안넘어가고 싶을 때)
			if($(this).hasClass('prev')){
				goToSlide(currentIndex - 1);
			} else {
				goToSlide(currentIndex + 1);
			}
		});
		
		$indicator.on('click', 'a', function(){
			if(! $(this).hasClass('active')) {
				goToSlide($(this).index());
			}
		});
		
		$container.on({
			mouseenter : stopTimer,
			mouseleave : startTimer
		});
		
		goToSlide(currentIndex);
		startTimer();
	});
});
