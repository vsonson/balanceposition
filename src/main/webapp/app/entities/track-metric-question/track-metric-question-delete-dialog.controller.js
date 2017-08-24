(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TrackMetricQuestionDeleteController',TrackMetricQuestionDeleteController);

    TrackMetricQuestionDeleteController.$inject = ['$uibModalInstance', 'entity', 'TrackMetricQuestion'];

    function TrackMetricQuestionDeleteController($uibModalInstance, entity, TrackMetricQuestion) {
        var vm = this;

        vm.trackMetricQuestion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TrackMetricQuestion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
