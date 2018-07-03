(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sleep-datum', {
            parent: 'entity',
            url: '/sleep-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SleepData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sleep-datum/sleep-data.html',
                    controller: 'SleepDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('sleep-datum-detail', {
            parent: 'sleep-datum',
            url: '/sleep-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SleepDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sleep-datum/sleep-datum-detail.html',
                    controller: 'SleepDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SleepDatum', function($stateParams, SleepDatum) {
                    return SleepDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sleep-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sleep-datum-detail.edit', {
            parent: 'sleep-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sleep-datum/sleep-datum-dialog.html',
                    controller: 'SleepDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SleepDatum', function(SleepDatum) {
                            return SleepDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sleep-datum.new', {
            parent: 'sleep-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sleep-datum/sleep-datum-dialog.html',
                    controller: 'SleepDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                durationHours: null,
                                feel: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sleep-datum', null, { reload: 'sleep-datum' });
                }, function() {
                    $state.go('sleep-datum');
                });
            }]
        })
        .state('sleep-datum.edit', {
            parent: 'sleep-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sleep-datum/sleep-datum-dialog.html',
                    controller: 'SleepDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SleepDatum', function(SleepDatum) {
                            return SleepDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sleep-datum', null, { reload: 'sleep-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sleep-datum.delete', {
            parent: 'sleep-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sleep-datum/sleep-datum-delete-dialog.html',
                    controller: 'SleepDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SleepDatum', function(SleepDatum) {
                            return SleepDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sleep-datum', null, { reload: 'sleep-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
