function addMaterialGroup(title,json,group_idx,isManualAdd) {
	var is_exist_group = false;
	if (group_idx && $("#divMaterialArea_"+group_idx).length) {
		//존재함
		is_exist_group = true;
	} else {
		if (!group_idx) group_idx = 0;
		$("[id^=divMaterialArea_]").each(function() {
            var idx = parseInt($(this).prop('id').replace('divMaterialArea_',''),10);
            group_idx = Math.max(group_idx,idx);
        });
        group_idx++;
	}
	if (is_exist_group) {
		var prev_title = $("#liMaterialGroup_"+group_idx+" [id=material_group_title_"+group_idx+"]").val();
		if ((prev_title == '' || prev_title == '재료') && title != '') {
			$("#liMaterialGroup_"+group_idx+" [id=material_group_title_"+group_idx+"]").val(title);
		}
	} else {
		var title_width = ($("#cok_reg_type").val() == 'edit') ? 190 : 210;
		var addbtn_style = ($("#cok_reg_type").val() == 'edit') ? 'padding:0 0 20px 240px; width:600px;' : 'padding:0 0 20px 350px; width:800px;';
		var str = '';
        str += '<li id="liMaterialGroup_'+group_idx+'">';
        str += ($("#cok_reg_type").val() == 'edit') ? '<p class="cont_tit6">' : '<p class="cont_tit6 st2 mag_r_15">';
        str += '<a href="#" class="btn-lineup"></a>';
		str += '<input type="text" name="material_group_title_'+group_idx+'" id="material_group_title_'+group_idx+'" value="'+title+'" class="form-control" style="font-weight:bold;font-size:18px;width:'+title_width+'px;">';
        str += '<span class="cont_tit_btn">';
		str += '<button id="btnAutoMaterialModal" data-toggle="modal" data-target="#divAutoMaterialModal" type="button" data-group_idx="'+group_idx+'" class="btn-sm btn-default"><span class="glyphicon glyphicon-import"></span> 한번에 넣기</button>';
		str += '<button type="button" onclick="delMaterialGroup('+group_idx+')" class="btn-sm btn-default"><span class="glyphicon glyphicon-remove"></span> 묶음삭제</button>';
        str += '</span>';
		str += '</p>';
        str += '<ul id="divMaterialArea_'+group_idx+'"></ul>';
        str += '<div class="btn_add" style="'+addbtn_style+'"><button type="button" onclick="addMaterial('+group_idx+')" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span>추가</button></div>';
        str += '</li>';

        $(str).appendTo("#divMaterialGroupArea");
	}

    for (var i=0; i<json.length; i++) {
		addMaterial(group_idx,json[i],'');
	}
	if (group_idx == 1) {
		if ($("#divMaterialArea_" + group_idx + " [id^=liMaterial_" + group_idx + "_]").length < 3) {
			for (var j = i; j < 3; j++) {
				addMaterial(group_idx, [], '');
			}
		}
	} else {
		if ($("#divMaterialArea_" + group_idx + " [id^=liMaterial_" + group_idx + "_]").length < 3) {
			for (var j = i; j < 2; j++) {
				addMaterial(group_idx, [], '');
			}
		}
	}
	$("#divMaterialGroupArea").sortable({
        handle: $('.btn-lineup')
    });
	if (isManualAdd && isManualAdd == '1') {
        $("#material_group_title_"+group_idx).focus();
    }

}
function delMaterialGroup(group_idx) {
	var cnt = 0;
	$("#divMaterialArea_"+group_idx+" [id^=cok_material_nm_"+group_idx+"_]").each(function() {
		if ($.trim($(this).val()) != '') {
			cnt++;
		}
	});
	var isOK = true;
	if (cnt > 0) {
		if(!confirm('['+$("#material_group_title_"+group_idx).val()+']을 삭제하시겠습니까?')) {
			isOK = false;
		}
	}
	if (isOK) {
		if ($("#divMaterialGroupArea [id^=liMaterialGroup_]").length == 1) {
            $("#liMaterialGroup_"+group_idx+" [id=material_group_title_"+group_idx+"]").val('');
            $("#divMaterialArea_"+group_idx+" [id^=liMaterial_"+group_idx+"_]").each(function(idx,obj) {
                var step = $(this).prop('id').replace('liMaterial_'+group_idx+'_','');
                if (idx < 3) {
                    $("#liMaterial_"+group_idx+"_"+step+" [id=cok_material_nm_"+group_idx+"_"+step).val('');
                    $("#liMaterial_"+group_idx+"_"+step+" [id=cok_material_amt_"+group_idx+"_"+step).val('');
                } else {
                    $("#liMaterial_"+group_idx+"_"+step).remove();
                }
            });
        } else {
            $("#divMaterialGroupArea [id=liMaterialGroup_"+group_idx+"]").fadeOut(200,function() {
                $(this).remove();
            });
        }
	}
}
function addMaterial(group_idx, init_json, prev_step){
    var step = 0;
    $("#divMaterialArea_"+group_idx+" [id^=liMaterial_"+group_idx+"_]").each(function(){
        var tmp = $(this).prop('id').replace('liMaterial_'+group_idx+'_', '');
        var tmp_step = parseInt(tmp, 10);
        step = Math.max(step, tmp_step);
    });
    step++;
    //alert(step);
    var w1 = ($("#cok_reg_type").val() == 'edit') ? 180 : 330;
	var w2 = ($("#cok_reg_type").val() == 'edit') ? 140 : 280;
    var str = '';
	str += '<li id="liMaterial_'+group_idx+'_'+step+'"><a href="#" class="btn-lineup"></a>';
    str += '<input type="text" name="cok_material_nm_'+group_idx+'[]" id="cok_material_nm_'+group_idx+'_'+step+'" class="form-control" style="width:'+w1+'px;">';
    str += '<input type="text" name="cok_material_amt_'+group_idx+'[]" id="cok_material_amt_'+group_idx+'_'+step+'" class="form-control" style="width:'+w2+'px;">';
    str += '<a id="btnMaterialDel_'+group_idx+'_'+step+'" href="javascript:delMaterial('+group_idx+','+step+')" class="btn-del" style="display:none"></a></li>';

    if (typeof prev_step == 'undefined' || prev_step === null || prev_step == 0) {
        $(str).appendTo('#divMaterialArea_'+group_idx);
    }
    else {
        $(str).insertAfter("#liMaterial_"+group_idx+"_" + prev_step);
    }

    if (typeof init_json !== 'undefined' && init_json !== null && init_json['mat_nm_material']) {
        $("#divMaterialArea_"+group_idx+" [id=cok_material_nm_" + group_idx + "_" + step + "]").val(init_json['mat_nm_material']);
    } else {
        $("#divMaterialArea_"+group_idx+" [id=cok_material_nm_" + group_idx + "_" + step + "]").attr('placeholder','예) '+_MATERIAL_SAMPLE[(step-1)%_MATERIAL_SAMPLE.length]['mat_nm_material']);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && (init_json['mat_no_amount'] || init_json['mat_tx_amount'])) {
		$("#divMaterialArea_"+group_idx+" [id=cok_material_amt_" + group_idx + "_" + step + "]").val((init_json['mat_no_amount'] ? init_json['mat_no_amount'] : '')+(init_json['mat_tx_amount'] ? init_json['mat_tx_amount'] : ''));
    } else {
        $("#divMaterialArea_"+group_idx+" [id=cok_material_amt_" + group_idx + "_" + step + "]").attr('placeholder','예) '+_MATERIAL_SAMPLE[(step-1)%_MATERIAL_SAMPLE.length]['mat_nm_amount']);
    }

    $("#divMaterialArea_"+group_idx+" [id=liMaterial_" + group_idx + "_" + step + "]").mouseover(function(){
        $(this).find('.btn-del').show();
    }).mouseout(function(){
        $(this).find('.btn-del').hide();
    });

    $("#divMaterialArea_"+group_idx).sortable({
        handle: $('.btn-lineup')
    });
    //$( "ul, li" ).disableSelection();
}
function delMaterial(group_idx,step) {
    $("#divMaterialArea_"+group_idx+" [id=liMaterial_"+group_idx+"_"+step+"]").fadeOut(200,function() {
        $(this).remove();
    });
}
function addSpice(prev_step, init_json){
    var step = 0;

    $("#divSpiceArea [id^=liSpice_]").each(function(){
        var tmp = $(this).prop('id').replace('liSpice_', '');
        var tmp_step = parseInt(tmp, 10);
        step = Math.max(step, tmp_step);
    });
    step++;
    //alert(step);
	var w = ($("#cok_reg_type").val() == 'edit') ? 190 : 300;
    var str = '<li id="liSpice_'+step+'"><a href="#" class="btn-lineup"></a>';
    str += '<input type="text" name="cok_spice_nm[]" id="cok_spice_nm_'+step+'" class="form-control" style="width:'+w+'px;">';
    str += '<input type="text" name="cok_spice_amt[]" id="cok_spice_amt_'+step+'" class="form-control" style="width:'+w+'px;">';
    str += '<a id="btnSpiceDel_'+step+'" href="javascript:delSpice('+step+')" class="btn-del" style="display:none"></a></li>';

    if (typeof prev_step == 'undefined' || prev_step === null || prev_step == 0) {
        $(str).appendTo('#divSpiceArea');
    }
    else {
        $(str).insertAfter("#liSpice_" + prev_step);
    }

    if (typeof init_json !== 'undefined' && init_json !== null && init_json['nm']) {
        $("#divSpiceArea [id=cok_spice_nm_" + step + "]").val(init_json['nm']);
    } else {
        $("#divSpiceArea [id=cok_spice_nm_" + step + "]").attr('placeholder','예) '+_SPICE_SAMPLE[(step-1)%_SPICE_SAMPLE.length]['nm']);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && init_json['amt']) {
        $("#divSpiceArea [id=cok_spice_amt_" + step + "]").val(init_json['amt']);
    } else {
        $("#divSpiceArea [id=cok_spice_amt_" + step + "]").attr('placeholder','예) '+_SPICE_SAMPLE[(step-1)%_SPICE_SAMPLE.length]['amt']);
    }

    $("#divSpiceArea [id=liSpice_" + step + "]").mouseover(function(){
        $(this).find('.btn-del').show();
    }).mouseout(function(){
        $(this).find('.btn-del').hide();
    });

	$("#divSpiceArea").sortable({
        handle: $('.btn-lineup')
    });
	//$( "ul, li" ).disableSelection();
}
function delSpice(step) {
    $("#divSpiceArea [id=liSpice_"+step+"]").fadeOut(200,function() {
        $(this).remove();
    });
}

function addStep(prev_step, init_json){
    var step = 0;
    //var obj_step = $(obj).parent().prop('id').replace('divStepBtn_','');
    $("#divStepArea [id^=divStepItem_]").each(function(){
        var tmp = $(this).prop('id').replace('divStepItem_', '');
        var tmp_step = parseInt(tmp, 10);
        step = Math.max(step, tmp_step);
    });
    step++;
    //alert(step);
    var str = $("#divStepTemplate").html().replace(/__STEP/g, step);
    var str = str.replace(/_STEP/g, '_' + step);


    if (typeof prev_step == 'undefined' || prev_step === null || prev_step == 0) {
        $(str).appendTo('#divStepArea');
    }
    else {
        $(str).insertAfter("#divStepItem_" + prev_step);
    }

    if (typeof init_json !== 'undefined' && init_json !== null && init_json['text']) {
        $("#divStepArea [id=step_text_" + step + "]").val(init_json['text']);
    } else {
		$("#divStepArea [id=step_text_" + step + "]").attr('placeholder','예) '+_STEP_SAMPLE[(step-1)%_STEP_SAMPLE.length]);
	}
    if (typeof init_json !== 'undefined' && init_json !== null && init_json['step_no']) {
        $("#divStepArea [id=step_no_" + step + "]").val(init_json['step_no']);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && init_json['photo']) {
        setStepPhoto('', init_json['photo'][0], init_json['photo'][0], step);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && (init_json['tip'] || init_json['material'] || init_json['fire'] || init_json['cooker'] || init_json['video'])) {
        $("#divStepArea [id=addStepInfoForm_" + step + "]").show();
    } else {
        $("#divStepArea [id=addStepInfoButs_" + step + "]").show();
    }

    if (typeof init_json !== 'undefined' && init_json !== null && init_json['tip']) {
        $("#divStepArea [id=stepForm_tip_" + step + "]").show();
        $("#divStepArea [id=step_tip_" + step + "]").val(init_json['tip']);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && init_json['material']) {
        $("#divStepArea [id=stepForm_material_" + step + "]").show();
        $("#divStepArea [id=step_material_" + step + "]").val(init_json['material']);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && init_json['fire']) {
        $("#divStepArea [id=stepForm_fire_" + step + "]").show();
        $("#divStepArea [id=step_fire_" + step + "]").val(init_json['fire']);
    }
    if (typeof init_json !== 'undefined' && init_json !== null && init_json['cooker']) {
        $("#divStepArea [id=stepForm_cooker_" + step + "]").show();
        $("#divStepArea [id=step_cooker_" + step + "]").val(init_json['cooker']);
    }

    if (typeof init_json !== 'undefined' && init_json !== null && init_json['video']) {
        $("#divStepArea [id=stepForm_video_" + step + "]").show();
        $("#divStepArea [id=step_video_" + step + "]").val(init_json['video']);
        $("#divStepArea [id=step_video_seq_" + step + "]").val(init_json['video_seq']);
    }

    $("#divStepArea [id=stepBtn_material_" + step + "]").click(function(){
        $("#divStepArea [id=stepForm_material_" + step + "]").show();
    });
    $("#divStepArea [id=stepBtn_cooker_" + step + "]").click(function(){
        $("#divStepArea [id=stepForm_cooker_" + step + "]").show();
    });
    $("#divStepArea [id=stepBtn_fire_" + step + "]").click(function(){
        $("#divStepArea [id=stepForm_fire_" + step + "]").show();
    });
    $("#divStepArea [id=stepBtn_tip_" + step + "]").click(function(){
        $("#divStepArea [id=stepForm_tip_" + step + "]").show();
    });
    $("#divStepArea [id=stepBtn_video_" + step + "]").click(function(){
        $("#divStepArea [id=stepForm_video_" + step + "]").show();
    });
    $("#divStepArea [id=stepBtn_all_" + step + "]").click(function(){
        $("#divStepArea [id=stepForm_material_" + step + "]").show();
        $("#divStepArea [id=stepForm_cooker_" + step + "]").show();
        $("#divStepArea [id=stepForm_fire_" + step + "]").show();
        $("#divStepArea [id=stepForm_tip_" + step + "]").show();
            });

    $("#divStepArea [id=divStepItem_" + step + "]").mouseover(function(){
        $(this).find('.step_btn').show();
    }).mouseout(function(){
        $(this).find('.step_btn').hide();
    });

    $("#divStepArea [id=divStepItem_" + step + "] .moveUp").click(function(){
		if ($(this).parents('.step').prevAll('.step').length !== 0) {
			$(this).parents('.step').insertBefore($(this).parents('.step').prev());
			remakeStepNumber();
		}
    });
    $("#divStepArea [id=divStepItem_" + step + "] .moveDown").click(function(){
		if ($(this).parents('.step').nextAll('.step').length !== 0) {
			$(this).parents('.step').insertAfter($(this).parents('.step').next());
			remakeStepNumber();
		}
    });
	$("#divStepArea").sortable({
        handle: ($("#cok_reg_type").val() == 'input') ? $(".cont_tit2_1") : $(".cont_tit2"),
        stop: function(event,ui) {
            remakeStepNumber();
        }
    });

	$("#divStepArea [id=divStepNum_" + step + "]").tooltip({
        'placement': 'top',
        'container': $('.recipe_regi'),
        'title': '드래그하면 순서를 변경할 수 있습니다.'
    });

	if ($("#cok_reg_type").val() == 'edit') {
		$("#divStepItem_"+step).droppable({
            accept: "#divLeftContent img, #divLeftContent span",
			drop: function( event, ui ) {
				//var src = ui.draggable.attr('src');
				var src = ($(ui.draggable).prop('tagName') == 'IMG') ? ui.draggable.attr('src') : ui.draggable.attr('img_src');
                var target_step = $(this).prop('id').replace('divStepItem_','');
				setStepPhoto('1',src,src,target_step);
            }
        });
	}

    bindEvent(document.getElementById("q_step_file_" + step), 'change', handlePhotoFiles);

    remakeStepNumber();
}
function bindEvent(el, eventName, eventHandler){

    if (el.addEventListener) {
        el.addEventListener(eventName, eventHandler, false);
    }
    else
        if (el.attachEvent) {
            el.attachEvent(eventName, eventHandler);
        }

}

function remakeStepNumber(){
    $("#divStepArea [id^=divStepItem_]").each(function(idx, obj){
        var step = $(this).prop('id').replace('divStepItem_', '');
        $("#divStepArea [id=divStepNum_" + step + "]").html('Step' + (idx + 1));
    });
}