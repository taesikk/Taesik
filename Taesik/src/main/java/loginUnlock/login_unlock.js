
'use strict';

var accountId = getUrlVar('');
var smsNumber = '';
var authenticateId = '';
var secondsToExpire = 0;
var authCount = 0;

var init = function() {
    var timer = null;
    var second = 1000;
    var minute = second * 60; // 인증번호 유효시간 타이머 변수

    var recaptchaToken = ''; // Google Recaptcha token

    if (REG_EXP_EMAIL.test(accountId) && smsNumber != '') {
        userMail.text(accountId);
    }
    sendAuthCodeButton.on('click', function() {

        if (!grecaptcha.getResponse(captchaClientId)) {
            $('#recaptcha').text('');
            return;
        }
        $('#captchaErrorMessage').text('');

        sendAuthCodeButton.text('');
        sendAuthCodeButton.addClass('disabled');
        var otpVendor = '';
        var body = {
            input: {
                country_code: countryCode,
                country_id: countryId
            },
            recaptchaToken: '',
        };
        if (mailRadio.prop('checked')) {
            otpVendor = 'email_otp';
            body.input.email = accountId;
        } else if (smsRadio.prop('checked')) {
            otpVendor = 'sms_otp';
        }

        if (grecaptcha.getResponse(captchaClientId)) {
            body.recaptchaToken = grecaptcha.getResponse(captchaClientId);
        }
        var requestURL = ';
        
        if (recaptchaToken) {
            body.recaptchaToken = recaptchaToken;
        }

        var successCallback = function(data) {
            var result = data;
            if (result) {
                startTimer(certTimer);
            } else {
                startTimer(singleCertTimer);
            }
        };
        var errorCallback = function(code, message) {
            console.log('error');
        };

        ajaxProc(requestURL, 'POST', JSON.stringify(body), true, successCallback, errorCallback);
    });

    singleCompleteBtn.on('click', function() {
        completeBtn.trigger('click');
    });

    completeBtn.on('click', function() {
        var requestURL = '';

        var successCallback = function(data) {
            if (data) {
            } else {
            }
        };
        var errorCallback = function(code, message) {
        }
        ajaxProc(requestURL, 'GET', '', true, successCallback, errorCallback);
    });

    // timer 시작 function
    var startTimer = function($certTimer) {
        stopTimer();
        $certTimer.removeClass('ct_hide');

        var now = new Date().getTime();
        var countTime = now + (secondsToExpire * second);
        timer = setInterval(function() {
            now = new Date().getTime();
            var distance = countTime - now;
            var count_minute = Math.floor((distance % (minute * 60)) / minute);
            var count_second = Math.floor((distance % minute) / second);
            if (count_minute < 10) {
                count_minute = '0' + count_minute;
            }

            if (count_second < 10) {
                count_second = '0' + count_second;
            }
            $certTimer.text(count_minute + ' : ' + count_second);
            if (distance < 0) {
                stopTimer();
            }
        }, 250);
    };

    var stopTimer = function() {
        if(timer) {
            clearInterval(timer);
            timer = null;
        }
    };

};