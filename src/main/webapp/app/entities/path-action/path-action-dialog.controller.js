(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathActionDialogController', PathActionDialogController);

    PathActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PathAction', 'PathStep', 'TrackMetric', 'Program'];

    function PathActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PathAction, PathStep, TrackMetric, Program) {
        var vm = this;

        vm.pathAction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pathsteps = PathStep.query();
        vm.trackmetrics = TrackMetric.query({filter: 'pathaction-is-null'});
        $q.all([vm.pathAction.$promise, vm.trackmetrics.$promise]).then(function() {
            if (!vm.pathAction.trackMetric || !vm.pathAction.trackMetric.id) {
                return $q.reject();
            }
            return TrackMetric.get({id : vm.pathAction.trackMetric.id}).$promise;
        }).then(function(trackMetric) {
            vm.trackmetrics.push(trackMetric);
        });
        vm.programs = Program.query({filter: 'pathaction-is-null'});
        $q.all([vm.pathAction.$promise, vm.programs.$promise]).then(function() {
            if (!vm.pathAction.program || !vm.pathAction.program.id) {
                return $q.reject();
            }
            return Program.get({id : vm.pathAction.program.id}).$promise;
        }).then(function(program) {
            vm.programs.push(program);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pathAction.id !== null) {
                PathAction.update(vm.pathAction, onSaveSuccess, onSaveError);
            } else {
                PathAction.save(vm.pathAction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:pathActionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
