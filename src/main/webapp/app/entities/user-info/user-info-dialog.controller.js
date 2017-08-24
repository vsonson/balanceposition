(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('UserInfoDialogController', UserInfoDialogController);

    UserInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'UserInfo', 'User', 'NetworkMember', 'MetricHistory', 'Note', 'ProgramHistory', 'UserNotification', 'WellnessHistory', 'IncentiveHistory'];

    function UserInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, UserInfo, User, NetworkMember, MetricHistory, Note, ProgramHistory, UserNotification, WellnessHistory, IncentiveHistory) {
        var vm = this;

        vm.userInfo = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.networkmembers = NetworkMember.query();
        vm.metrichistories = MetricHistory.query();
        vm.notes = Note.query();
        vm.programhistories = ProgramHistory.query();
        vm.usernotifications = UserNotification.query();
        vm.wellnesshistories = WellnessHistory.query();
        vm.incentivehistories = IncentiveHistory.query();

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
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        userInfo.profilePic = base64Data;
                        userInfo.profilePicContentType = $file.type;
                    });
                });
            }
        };

    }
})();
