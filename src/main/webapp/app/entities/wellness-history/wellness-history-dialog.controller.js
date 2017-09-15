(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('WellnessHistoryDialogController', WellnessHistoryDialogController);

    WellnessHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'WellnessHistory', 'WellnessItem', 'UserInfo'];

    function WellnessHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, WellnessHistory, WellnessItem, UserInfo) {
        var vm = this;

        vm.wellnessHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.wellnessitems = WellnessItem.query({filter: 'wellnesshistory-is-null'});
        $q.all([vm.wellnessHistory.$promise, vm.wellnessitems.$promise]).then(function() {
            if (!vm.wellnessHistory.wellnessItem || !vm.wellnessHistory.wellnessItem.id) {
                return $q.reject();
            }
            return WellnessItem.get({id : vm.wellnessHistory.wellnessItem.id}).$promise;
        }).then(function(wellnessItem) {
            vm.wellnessitems.push(wellnessItem);
        });
        vm.userinfos = UserInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wellnessHistory.id !== null) {
                WellnessHistory.update(vm.wellnessHistory, onSaveSuccess, onSaveError);
            } else {
                WellnessHistory.save(vm.wellnessHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:wellnessHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
