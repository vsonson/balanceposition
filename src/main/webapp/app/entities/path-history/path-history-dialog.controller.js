(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathHistoryDialogController', PathHistoryDialogController);

    PathHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PathHistory', 'PathWay', 'PathStep', 'PathAction'];

    function PathHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PathHistory, PathWay, PathStep, PathAction) {
        var vm = this;

        vm.pathHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.pathways = PathWay.query({filter: 'pathhistory-is-null'});
        $q.all([vm.pathHistory.$promise, vm.pathways.$promise]).then(function() {
            if (!vm.pathHistory.pathway || !vm.pathHistory.pathway.id) {
                return $q.reject();
            }
            return PathWay.get({id : vm.pathHistory.pathway.id}).$promise;
        }).then(function(pathway) {
            vm.pathways.push(pathway);
        });
        vm.pathsteps = PathStep.query({filter: 'pathhistory-is-null'});
        $q.all([vm.pathHistory.$promise, vm.pathsteps.$promise]).then(function() {
            if (!vm.pathHistory.pathStep || !vm.pathHistory.pathStep.id) {
                return $q.reject();
            }
            return PathStep.get({id : vm.pathHistory.pathStep.id}).$promise;
        }).then(function(pathStep) {
            vm.pathsteps.push(pathStep);
        });
        vm.pathactions = PathAction.query({filter: 'pathhistory-is-null'});
        $q.all([vm.pathHistory.$promise, vm.pathactions.$promise]).then(function() {
            if (!vm.pathHistory.pathAction || !vm.pathHistory.pathAction.id) {
                return $q.reject();
            }
            return PathAction.get({id : vm.pathHistory.pathAction.id}).$promise;
        }).then(function(pathAction) {
            vm.pathactions.push(pathAction);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pathHistory.id !== null) {
                PathHistory.update(vm.pathHistory, onSaveSuccess, onSaveError);
            } else {
                PathHistory.save(vm.pathHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:pathHistoryUpdate', result);
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
