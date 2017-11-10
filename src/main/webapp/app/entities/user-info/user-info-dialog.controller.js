(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('UserInfoDialogController', UserInfoDialogController);

    UserInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'UserInfo', 'User', 'NetworkMember', 'MetricHistory', 'ProgramHistory', 'Note', 'UserNotification', 'WellnessHistory', 'IncentiveHistory', 'QuoteOfTheDayHistory'];

    function UserInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, UserInfo, User, NetworkMember, MetricHistory, ProgramHistory, Note, UserNotification, WellnessHistory, IncentiveHistory, QuoteOfTheDayHistory) {
        var vm = this;

        vm.userInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.networkmembers = NetworkMember.query();
        vm.metrichistories = MetricHistory.query();
        vm.programhistories = ProgramHistory.query();
        vm.notes = Note.query();
        vm.usernotifications = UserNotification.query();
        vm.wellnesshistories = WellnessHistory.query();
        vm.incentivehistories = IncentiveHistory.query();
        vm.quoteofthedayhistories = QuoteOfTheDayHistory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userInfo.id !== null) {
                UserInfo.update(vm.userInfo, onSaveSuccess, onSaveError);
            } else {
                UserInfo.save(vm.userInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:userInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setProfilePic = function ($file, userInfo) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        userInfo.profilePic = base64Data;
                        userInfo.profilePicContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.dateOfBirth = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
