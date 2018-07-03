(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('performance-datum', {
            parent: 'entity',
            url: '/performance-datum',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PerformanceData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/performance-datum/performance-data.html',
                    controller: 'PerformanceDatumController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('performance-datum-detail', {
            parent: 'performance-datum',
            url: '/performance-datum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PerformanceDatum'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/performance-datum/performance-datum-detail.html',
                    controller: 'PerformanceDatumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PerformanceDatum', function($stateParams, PerformanceDatum) {
                    return PerformanceDatum.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'performance-datum',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('performance-datum-detail.edit', {
            parent: 'performance-datum-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performance-datum/performance-datum-dialog.html',
                    controller: 'PerformanceDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PerformanceDatum', function(PerformanceDatum) {
                            return PerformanceDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('performance-datum.new', {
            parent: 'performance-datum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performance-datum/performance-datum-dialog.html',
                    controller: 'PerformanceDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                feel: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('performance-datum', null, { reload: 'performance-datum' });
                }, function() {
                    $state.go('performance-datum');
                });
            }]
        })
        .state('performance-datum.edit', {
            parent: 'performance-datum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performance-datum/performance-datum-dialog.html',
                    controller: 'PerformanceDatumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PerformanceDatum', function(PerformanceDatum) {
                            return PerformanceDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('performance-datum', null, { reload: 'performance-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('performance-datum.delete', {
            parent: 'performance-datum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performance-datum/performance-datum-delete-dialog.html',
                    controller: 'PerformanceDatumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PerformanceDatum', function(PerformanceDatum) {
                            return PerformanceDatum.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('performance-datum', null, { reload: 'performance-datum' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
